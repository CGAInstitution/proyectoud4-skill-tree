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
        isDragging = false;
        if (activeNoteContainer) {
            activeNoteContainer.style.cursor = "move";
            activeNoteContainer.style.zIndex = "1";
            activeNoteContainer = null;
        }
    });
});
