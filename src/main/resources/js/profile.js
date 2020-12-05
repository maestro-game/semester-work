window.addEventListener("load", () => {
    function convertToForm(e) {
        e.preventDefault();

        let savedNode = e.currentTarget;

        async function changeProfile(field, text) {
            // const data = new URLSearchParams();
            // data.append("field", field);
            // data.append("data", text);
            await fetch(`/info?field=${field}&data=${text}`, {
                method: "put",
                // body: data
            });
        }

        async function handler(ev) {
            ev.preventDefault();

            if (ev.currentTarget === button) {
                button.value = input.value;
            }

            const field = ev.currentTarget.id;
            const text = ev.currentTarget.value;
            let parent = ev.currentTarget.parentNode;
            try {
                savedNode.innerHTML = text;
                await changeProfile(field, text);

            } catch (err) {
                console.error(err);
            } finally {
                parent.innerHTML = '';
                parent.appendChild(savedNode);
            }
        }

        // TODO: create selector for date

        let input = document.createElement("input");
        input.type = "text";
        input.id = e.currentTarget.id;
        input.addEventListener("keypress", (ke) => {
            if (ke.key === 'Enter') handler(ke)
        });

        // CREATING TICK SVG BUTTON
        let button = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        button.setAttributeNS(null, "viewBox", "0 0 24 24");
        button.setAttributeNS(null, "fill", "green");
        button.setAttributeNS(null, "width", "32px");
        button.setAttributeNS(null, "height", "32px");
        let path1 = document.createElementNS("http://www.w3.org/2000/svg", "path");
        path1.setAttribute("d", "M0 0h24v24H0z");
        path1.setAttributeNS(null, "fill", "none");
        let path2 = document.createElementNS("http://www.w3.org/2000/svg", "path");
        path2.setAttribute("d", "M9 16.2L4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4L9 16.2z");
        button.appendChild(path1);
        button.appendChild(path2);
        button.style.cursor = "pointer";
        button.id = e.currentTarget.id;
        button.addEventListener("click", handler);

        let parent = e.currentTarget.parentNode;
        parent.appendChild(button);
        parent.replaceChild(input, e.currentTarget);
    }

    // TODO: add event listener to name/surname/middleName (and optional for categories&users)
    let neededNodes = document.querySelectorAll(".owner__empty");
    neededNodes.forEach((_) => _.addEventListener("click", convertToForm))
})
