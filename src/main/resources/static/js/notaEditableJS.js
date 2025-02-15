const app = Vue.createApp({
    data() {
        return {
            idNota: null,
            titleContent: null,
            descripcionContent:null,
            selectedColor: null,
            showPicker: false,
            colors: ['#ffadad', '#ffd6a5', '#fae890','#bcfdae', '#9bf6ff', '#a0c4ff', '#bdb2ff', '#ffc6ff'],
            pickerPosition: { top: '0px', left: '0px' },
            correo: '',
            modalVisible: false
        };
    },
    mounted() {
        if (this.$refs.paper){
            this.idNota = this.$refs.paper.getAttribute("data-idNota");
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
            this.updateColorInBackend(color);
        },
        updateColorInBackend(color) {
            fetch(`/notas/${this.idNota}/actualizar-color`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ color })
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
        mostrarModal() {
            this.modalVisible = true;
        },


        cerrarModal() {
            this.modalVisible = false;
        },

        compartirNota() {
            if (this.correo) {
                // Suponemos que el correo está asociado a un usuario
                fetch('/notas/compartir', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        idNota: this.idNota,
                        correo: this.correo // Asegúrate de que el backend pueda buscar el usuario por correo
                    })
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            alert(`Nota compartida con ${this.correo}`);
                            this.cerrarModal(); // Cierra el modal después de compartir
                        } else {
                            alert("Hubo un error al compartir la nota.");
                        }
                    })
                    .catch(error => {
                        console.error("Error al compartir:", error);
                        alert("Error al compartir la nota.");
                    });
            } else {
                alert("Por favor ingresa un correo.");
            }
        },
        downloadNota(){
            fetch(`/notas/${this.idNota}/descargar`, {
                method: "GET"
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Error al descargar la nota");
                    }
                    return response.blob();
                })
                .then(blob => {
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement("a");
                    a.href = url;
                    a.download = this.titleContent.replaceAll(" ", "_")+".md";
                    document.body.appendChild(a);
                    a.click();
                    window.URL.revokeObjectURL(url);
                })
                .catch(error => {
                    console.error("Error al descargar:", error);
                    alert("Error al descargar la nota.");
                });
        }

    }
});

app.mount("#app");