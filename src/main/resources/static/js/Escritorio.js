document.addEventListener("DOMContentLoaded", function () {
    let isDragging = false;
    let offsetX = 0;
    let offsetY = 0;
    let activeNoteContainer = null;

    document.querySelectorAll(".note-container > div").forEach(noteContainer => {
        noteContainer.addEventListener("mousedown", (event) => {
            isDragging = true;
            activeNoteContainer = noteContainer;

            offsetX = event.clientX - noteContainer.getBoundingClientRect().left;
            offsetY = event.clientY - noteContainer.getBoundingClientRect().top;

            // Asegurar que la nota esté en primer plano
            noteContainer.style.zIndex = "1000";
            noteContainer.style.cursor = "grabbing";
        });
    });

    document.addEventListener("mousemove", (event) => {
        if (isDragging && activeNoteContainer) {
            activeNoteContainer.style.left = `${event.clientX - offsetX}px`;
            activeNoteContainer.style.top = `${event.clientY - offsetY}px`;
        }
    });

    document.addEventListener("mouseup", () => {
      if (isDragging && activeNoteContainer) {
                 const idNota = activeNoteContainer.getAttribute("data-id");  // Se asume que la nota tiene un atributo 'data-id'
                 const newPosX = event.clientX - offsetX;
                 const newPosY = event.clientY - offsetY;

                 // Enviar la nueva posición al servidor
                 fetch(`/notas/${idNota}/actualizar-posicion`, {
                     method: "POST",
                     headers: {
                         "Content-Type": "application/json"
                     },
                     body: JSON.stringify({
                         posicionX: newPosX,
                         posicionY: newPosY
                     })
                 })
                 .then(response => response.json())
                 .then(data => {
                     if (data.success) {
                         console.log("Posición actualizada correctamente");
                     } else {
                         console.log("Error al actualizar la posición");
                     }
                 })
                 .catch(error => {
                     console.error("Error de red", error);
                 });

                 // Restaurar el estilo de la nota
                 activeNoteContainer.style.cursor = "move";
                 activeNoteContainer.style.zIndex = "1";
                 isDragging = false;
                 activeNoteContainer = null;
             }




    });
});
