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
            noteContainer.style.zIndex = "2";
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


    document.querySelectorAll(".note-container > div").forEach(note => {
      
        note.addEventListener("contextmenu", function (event) {
            event.preventDefault();
            activeNoteContainer = note;
            selectedNote = note.closest('.note-container');
            const noteId = activeNoteContainer.getAttribute("data-id");

            

            console.log("Nota seleccionada con ID: ", noteId);


            contextMenu.style.top = `${event.pageY}px`;
            contextMenu.style.left = `${event.pageX}px`;
            contextMenu.style.display = "block";

            // Establecer el data-id en el menú contextual para usarlo después
            contextMenu.setAttribute('data-id', noteId);
        });
    });

    // Cerrar el menú cuando se haga clic en cualquier lugar
    document.addEventListener("click", function () {
        contextMenu.style.display = "none";
    });
    document.getElementById("delete-note").addEventListener("click", function () {
        const noteId = contextMenu.getAttribute("data-id");

        if (noteId) {
            fetch(`/notas/${noteId}/eliminar`, { method: "DELETE" })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        console.log(`Nota con ID ${noteId} eliminada`);
                        const noteElement = document.querySelector(`.note-container > div[data-id='${noteId}']`);
                        if (noteElement) {
                            noteElement.remove(); // Elimina la nota del DOM
                        }
                    } else {
                        console.error("Error al eliminar la nota");
                    }
                })
                .catch(error => console.error("Error:", error));
        }
    });

});

document.addEventListener("DOMContentLoaded", function () {
    let isToggleMenuShowing = false;
    const showToggleButton = document.getElementById("show-toggle-menu-button");
    const closeToggleButton = document.getElementById("close-toggle-menu-button")
    const toggleMenu = document.getElementById("toggle-menu");

    showToggleButton.addEventListener("click", () => {
        toggleMenu.classList.toggle("show");
    });

    closeToggleButton.addEventListener("click", () => {
        toggleMenu.classList.toggle("show");
    });
});

function changeEscritorio(element) {
    fetch("escritorio/change", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            idEscritorio: element.dataset.id
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            window.location.reload();
        } else {
            console.log("Error al cambiar de escritorio");
        }
    })
    .catch(error => {
        console.log("Error al conectarse con el servidor", error);
    })
}

function viewNota(element) {
    let id = element.dataset.id;
    window.location.href = 'notas/' + id;
}

function openCreateEscritorioWindow(element) {
    const mainWindows = window;
    const height = 500;
    const width = 500;
    const left = (screen.width - width) / 2;
    const top = (screen.height - height) / 4;
    const newWindow = window.open("escritorio/create","Nuevo escritorio",'resizable=yes, width=' + width
        + ', height=' + height + ', top='
        + top + ', left=' + left);
}