const app = Vue.createApp({
    data() {
        return {
            selectedColor: '#ffffff', // Default color
            showPicker: false,
            colors: ['#ffadad', '#ffd6a5', '#bcfdae', '#9bf6ff', '#a0c4ff', '#bdb2ff', '#ffc6ff'],
            pickerPosition: { top: '0px', left: '0px' } // Position of the color picker
        };
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
        }
    }
});

app.mount("#app");