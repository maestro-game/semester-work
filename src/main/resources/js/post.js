window.addEventListener("load", () =>{
    document.querySelector(".img__post").addEventListener("mousehover", () => {
        let desc = document.querySelector(".description").style;
        desc.display = "block";
        desc.position = "absolute";
        desc.top = "0";
        desc.background = "#228b5e";
        desc.color = "white";
    });

    document.querySelector(".description__change").addEventListener("click", () => {
        let neededNode = document.querySelector(".description");
        let form = document.createElement("form");
        form.action = "/post";
        form.id = ""
        // neededNode
    })
});
