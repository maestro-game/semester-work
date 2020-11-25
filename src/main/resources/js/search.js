window.addEventListener("load", () => {
    const MAX_TAXON = 8;

    async function postSearch(taxon, id, type = 'getCats', page = null) {
        let data = new URLSearchParams();
        data.append("type", type);
        data.append("id", id.toString());
        data.append("taxon", taxon.toString());

        if (type === 'category') {
            data.append("page", page != null ? page.toString() : String(1));
        }

        const response = await fetch("/search", {
            method: "post",
            body: data
        });

        return response.json();
    }

    let page = 1;

    async function searchHandler(e) {
        e.preventDefault();

        const activeSelect = document.querySelector(".active");

        console.log(activeSelect.id + " " + activeSelect.value);

        const responseArray = await postSearch(String(Number(activeSelect.id) - 1), activeSelect.value, 'category', page);

        let neededNode = document.querySelector(".search__result");
        let resStr = `<h2 class="entry__page">Page: ${page}</h2>`;
        for (const entry of responseArray) {
            resStr += `<a class="entry__link" href="/post/${entry["id"]}">`;
            resStr += `<p class="entry__author">${entry["author"]["name"] + " " + entry["author"]["surname"]}</p>`;
            resStr += `<img class="entry__image" src="/files/images/posts/${entry["id"]}/${entry["id"]}.${entry["image"]}"  alt="картинка поста"/>`;
            resStr += `<p class="entry__specie">${entry["specie"]["name"]}</p>`;
            resStr += `</a>`;
        }
        neededNode.innerHTML = resStr;

        // page next and page prev buttons
        let dots = document.querySelector(".search__dots");
        let nextBefore, pastBefore;
        if (dots.childElementCount !== 0) {
            nextBefore = document.querySelector(".search__next");
            pastBefore = document.querySelector(".search__past");
        }
        let next = document.createElement("div");
        next.classList.add("search__next");
        next.innerHTML = "[NEXT]";
        next.addEventListener("click", (e) => {
            ++page;
            searchHandler(e)
        });
        let past = document.createElement("div");
        past.classList.add("search__past");
        past.innerHTML = "[PAST]";
        past.addEventListener("click", (e) => {
            page > 0 ? --page : page;
            searchHandler(e)
        });
        if (dots.childElementCount === 0) {
            dots.appendChild(past);
            dots.appendChild(next);
        } else {
            dots.replaceChild(past, pastBefore);
            dots.replaceChild(next, nextBefore);
        }
    }

    async function getCatHandler(e) {
        e.preventDefault();

        let select = e.currentTarget;
        const taxon = select.id.toString();

        let formNode = document.querySelector(".search__form");

        if (taxon !== String(MAX_TAXON + 1)) {

            const responseArray = taxon === "1" ? await postSearch(taxon, 0) : await postSearch(taxon, select.value);


            // if previous select changed, delete all consequent <select> entries
            if (select !== formNode.lastElementChild) {
                let last;
                while ((last = formNode.lastElementChild)) {
                    if (last === select) {
                        break;
                    }
                    formNode.removeChild(last);
                }
            }

            let newSelect = document.createElement("select");
            newSelect.classList.add("search__select");
            newSelect.id = (Number(taxon) + 1).toString();

            let newOption = document.createElement("option");
            newOption.value = String(0);
            newOption.innerHTML = taxon === "1" ? "Выберите категорию" : "Выберите категорию или нажмите \"поиск\"";
            newSelect.appendChild(newOption);
            for (const entry of responseArray) {
                newOption = document.createElement("option");
                newOption.value = entry["id"];
                newOption.innerHTML = entry["name"];
                newSelect.appendChild(newOption);
            }

            if (Number(taxon) <= MAX_TAXON) {
                newSelect.addEventListener("change", getCatHandler);
            }

            formNode.appendChild(newSelect);
        }

        if (taxon === "2") {
            let buttonToSearch = document.querySelector(".search__btn");
            buttonToSearch.addEventListener("click", searchHandler);
        }

        if (taxon === String(MAX_TAXON + 1)) {
            formNode.lastElementChild.classList.add("active");
            formNode.lastElementChild.previousElementSibling.classList.remove("active");
        } else if (taxon === String(1)) {
            formNode.lastElementChild.classList.add("active");
        } else {
            formNode.lastElementChild.previousElementSibling.classList.add("active");
            formNode.lastElementChild.previousElementSibling.previousElementSibling.classList.remove("active");
        }
    }

    let selectNode = document.querySelector(".search__select");

    selectNode.addEventListener("change", getCatHandler);
    selectNode.dispatchEvent(new Event("change"));
});
