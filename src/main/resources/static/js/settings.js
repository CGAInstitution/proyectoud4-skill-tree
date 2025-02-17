const app = Vue.createApp({
    data() {
        return {
            nombre: null,
            apellidos:null,
            email:null,
            contrasenia:null
        };
    },
    mounted() {
        if (this.$refs.paper) {
            this.idNota = this.$refs.paper.getAttribute("data-idNota");
            this.selectedColor = '#' + this.$refs.paper.getAttribute("data-color");
        } else {
            console.error("Referencia paper no encontrada")
        }

        if (this.$refs.titulo) {
            this.titleContent = this.$refs.titulo.getAttribute("data-titleContent") || "";

        } else {
            console.error("Referencia titulo no encontrada")
        }

        if (this.$refs.descripcion) {
            this.descripcionContent = this.$refs.descripcion.getAttribute("data-desc-content") || "";

        } else {
            console.error("Referencia descripción no encontrada")
        }
    }
});
