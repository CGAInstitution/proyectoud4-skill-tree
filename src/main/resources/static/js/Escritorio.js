document.addEventListener("DOMContentLoaded", function () {
    let isDragging = false;
    let offsetX = 0;
    let offsetY = 0;
    let activeNoteContainer = null;

    document.querySelectorAll(".note-container > div").forEach(noteContainer => {
        noteContainer.addEventListener("mousedown", (event) => {
            event.preventDefault();
            isDragging = true;
            activeNoteContainer = noteContainer;

            offsetX = event.clientX - noteContainer.getBoundingClientRect().left;
            offsetY = event.clientY - noteContainer.getBoundingClientRect().top;


            document.body.style.userSelect = "none";


            // Asegurar que la nota esté en primer plano
            noteContainer.style.zIndex = "1000";
            noteContainer.style.cursor = "grabbing";
        });
    });

    document.addEventListener("mousemove", (event) => {
        if (isDragging && activeNoteContainer) {
            event.preventDefault();
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

                 document.body.style.userSelect = "";

                 // Restaurar el estilo de la nota
                 activeNoteContainer.style.cursor = "move";
                 activeNoteContainer.style.zIndex = "1";
                 isDragging = false;
                 activeNoteContainer = null;
             }




    });
});

document.addEventListener("DOMContentLoaded", function () {
    const contextMenu = document.getElementById("context-menu");
    let selectedNote = null;

   document.querySelectorAll(".note-container").forEach(noteContainer => {
       noteContainer.addEventListener("contextmenu", function (event) {
           event.preventDefault(); // Evita el menú del navegador

           selectedNote = noteContainer;  // Aseguramos que estamos seleccionando el contenedor, no solo el div .note
           console.log("Nota seleccionada: ", selectedNote);  // Verifica que se seleccione correctamente

           contextMenu.style.top = `${event.pageY}px`;
           contextMenu.style.left = `${event.pageX}px`;
           contextMenu.style.display = "block";
       });
   });


    document.addEventListener("click", function () {
        contextMenu.style.display = "none";
    });




        
    });