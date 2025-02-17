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
    const contextMenuNote = document.getElementById("context-menu-note"); // Menú para editar y borrar
    const contextMenuCreate = document.getElementById("context-menu-create"); // Menú para crear nota
    let selectedNote = null;
    let activeNoteContainer = null;
    let createNoteX = null;
    let createNoteY = null;

    document.querySelectorAll(".note-container > div").forEach(note => {
        note.addEventListener("contextmenu", function (event) {
            console.log("hola")
            event.preventDefault();
            activeNoteContainer = note;
            selectedNote = note.closest('.note-container');
            const noteId = activeNoteContainer.getAttribute("data-id");

            contextMenuNote.style.top = `${event.pageY}px`;
            contextMenuNote.style.left = `${event.pageX}px`;
            contextMenuNote.style.display = "block";

            contextMenuNote.setAttribute('data-id', noteId);

            contextMenuCreate.style.display = "none";
        });
    });

    document.querySelector("body").addEventListener("contextmenu", function (event) {
            console.log("adios")
        if (!event.target.closest('.note')) {
            event.preventDefault();
            createNoteX = event.pageX;
            createNoteY = event.pageY;

            contextMenuCreate.style.top = `${event.pageY}px`;
            contextMenuCreate.style.left = `${event.pageX}px`;
            contextMenuCreate.style.display = "block";

            contextMenuNote.style.display = "none";
        }
    });

    document.addEventListener("click", function () {
        contextMenuNote.style.display = "none";
        contextMenuCreate.style.display = "none";
    });

    document.getElementById("create-note").addEventListener("click", function () {
        console.log(createNoteX,createNoteY);
        let posX = createNoteX;
        let posY = createNoteY;
        console.log(posX,posY);
        if (!posX || !posY) {
            posX = 500;
            posY = 500;
        }
        const url = `/notas/nueva?posicionX=${posX}&posicionY=${posY}`;

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ posicionX: posX, posicionY: posY })
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = response.url;
                } else {
                    console.error("Error al crear la nota");
                }
            })
            .catch(error => {
                console.error("Error al hacer la solicitud:", error);
            });
    });


    document.getElementById("delete-note").addEventListener("click", function () {
        const noteId = contextMenuNote.getAttribute("data-id");

        if (noteId) {
            fetch(`/notas/${noteId}/eliminar`, { method: "DELETE" })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        console.log(`Nota con ID ${noteId} eliminada`);
                        const noteElement = document.querySelector(`.note-container > div[data-id='${noteId}']`);
                        if (noteElement) {
                            noteElement.remove();
                        }
                    } else {
                        console.error("Error al eliminar la nota");
                    }
                })
                .catch(error => console.error("Error:", error));
        }
    });
    document.getElementById("edit-note").addEventListener("click", function () {
        const noteId = contextMenuNote.getAttribute("data-id");

        if (noteId) {
            window.location.href = `/notas/${noteId}`;
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