const app = Vue.createApp({
    data() {
        return {
            idNota: null,
            titleContent: null,
            descripcionContent:null,
            selectedColor: null,
            showPicker: false,
            colors: ['#ffadad', '#ffd6a5', '#fae890','#bcfdae', '#9bf6ff', '#a0c4ff', '#bdb2ff', '#ffc6ff'],
            pickerPosition: { top: '0px', left: '0px' }
        };
    },
    mounted() {
        console.log("mounted")

        if (this.$refs.paper){
            this.idNota = this.$refs.paper.getAttribute("data-idNota");
            console.log(this.idNota)
            this.selectedColor ='#'+ this.$refs.paper.getAttribute("data-color");
        }
        else {
            console.error("Referencia paper no encontrada")
        }

        if(this.$refs.titulo){
            this.titleContent = this.$refs.titulo.getAttribute("data-titleContent") || "";

        }
        else {
            console.error("Referencia titulo no encontrada")
        }

        if(this.$refs.descripcion){
            this.descripcionContent =this.$refs.descripcion.getAttribute("data-desc-content") || "";

        }else{
            console.error("Referencia descripción no encontrada")
        }
    },
    watch: {
        selectedColor(newColor) {
            // Change the background color of the body whenever selectedColor is updated
            document.body.style.backgroundColor = this.lightenColor(newColor, 50);
            document.querySelector('hr').style.backgroundColor = this.darkerColor(newColor, 20);
        }
    },
    methods: {
        toggleColorPicker() {
            this.showPicker = !this.showPicker;
            if (this.showPicker) {
                this.calculatePickerPosition();
            }
        },
        selectColor(color) {
            this.selectedColor = color;
            this.showPicker = false;

            this.sendColorToBackend(this.selectedColor)
        },
        calculatePickerPosition() {
            // Get the button's position
            const button = document.getElementById('btnColor');
            const rect = button.getBoundingClientRect();

            // Calculate the position for the color picker to appear directly below the button
            this.pickerPosition = {
                top: `${rect.bottom + window.scrollY}px`,
                left: `${rect.left + window.scrollX - 4}px`
            };
        },
        lightenColor(hex, percent) {
            let r = parseInt(hex.substring(1, 3), 16);
            let g = parseInt(hex.substring(3, 5), 16);
            let b = parseInt(hex.substring(5, 7), 16);
            r = Math.min(255, Math.floor(r + (255 - r) * (percent / 100)));
            g = Math.min(255, Math.floor(g + (255 - g) * (percent / 100)));
            b = Math.min(255, Math.floor(b + (255 - b) * (percent / 100)));
            return `rgb(${r}, ${g}, ${b})`;
        },
        darkerColor(hex, percent) {
            let r = parseInt(hex.substring(1, 3), 16);
            let g = parseInt(hex.substring(3, 5), 16);
            let b = parseInt(hex.substring(5, 7), 16);
            const amount = Math.round(2.55 * percent);
            r = Math.max(0, r - amount);
            g = Math.max(0, g - amount);
            b = Math.max(0, b - amount);
            return `rgb(${r}, ${g}, ${b})`;
        },
        updateTitleContent(event){
            this.titleContent = event.target.innerText;
            // Clear the previous timer if it exists
            if (this.updateTimerTitle) {
                clearTimeout(this.updateTimerTitle);
            }

            // Set a new timer to send the data after 5 seconds
            this.updateTimerTitle = setTimeout(() => {
                this.sendTitleToBackend(this.titleContent);
            }, 5000); // 5000 milliseconds = 5 seconds
        },
        sendTitleToBackend(nuevoTitulo) {
            // Send the data to the backend using fetch
            fetch(`/notas/${this.idNota}/actualizar-titulo`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({titulo:nuevoTitulo})
            })
            .then(response => {
                if (response.ok) {
                    console.log("Título actualizado correctamente");
                } else {
                    console.error("Error al actualizar el título");
                }
            })
            .catch(error => {
                console.error("Error de red:", error);
            });

            // Reset the timer ID
            this.updateTimer = null;
        },
        updateDescriptionContent(event){
            this.descripcionContent = event.target.innerText;
            // Clear the previous timer if it exists
            if (this.updateTimer) {
                clearTimeout(this.updateTimer);
            }

            // Set a new timer to send the data after 5 seconds
            this.updateTimer = setTimeout(() => {
                this.sendDescriptionToBackend(this.descripcionContent);
            }, 5000); // 5000 milliseconds = 5 seconds
        },
        sendDescriptionToBackend(nuevaDescripcion) {
            fetch(`/notas/${this.idNota}/actualizar-descripcion`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({descripcion:nuevaDescripcion})
            })
                .then(response => {
                    if (response.ok) {
                        console.log("Descripcion actualizada correctamente");
                    } else {
                        console.error("Error al actualizar la descripcion");
                    }
                })
                .catch(error => {
                    console.error("Error de red:", error);
                });

            // Reset the timer ID
            this.updateTimer = null;
        },
        sendColorToBackend(nuevoColor){
            fetch(`/notas/${this.idNota}/actualizar-color`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({color:nuevoColor})
            })
                .then(response => {
                    if (response.ok) {
                        console.log("Color actualizado correctamente");
                    } else {
                        console.error("Error al actualizar el color");
                    }
                })
                .catch(error => {
                    console.error("Error de red:", error);
                });

        }
    }
});

app.mount("#app");