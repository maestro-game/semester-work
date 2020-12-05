<#include "base.ftl">
<@html "post" "Пост" true true>
    <#setting date_format="dd MMM в HH:mm:ss">
    <#if user??>
        <script>
            async function descriptionChange(e) {
                e.preventDefault();

                let form = e.currentTarget;
                let data = new URLSearchParams(new FormData(form));
                data.append("id", "${post.getId()}");
                console.log(data.get("id"));
                const response = await postFormData("/post", data, "put");
                form.parentNode.innerHTML = `
                <div class="img__description description">
                    <p class="description__text">` + response + `</p>
                    <div class="description__default">
                        <p class="description__change" onclick="showDescriptionChange()">изменить</p>
                        <p class="description__delete">удалить</p>
                    </div>
                </div>`;
            }

            function showDescriptionChange() {
                let node = document.querySelector(".img__description");
                let form = document.createElement("form");
                form.action = "/post";
                form.innerHTML = `
                    <input type="text" id="text" name="text" />
                    <input type="submit" value="Изменить">
                `;
                form.addEventListener("submit", descriptionChange);
                node.innerHTML = "";
                node.appendChild(form);
            }

            async function like() {
                const data = new URLSearchParams();
                console.log(${post.getId()});
                data.append("postId", '${post.getId()}');
                let likesImg = document.querySelector(".likes__img");
                let likesCount = document.querySelector(".likes__count");
                if (likesImg.classList.contains("liked")) {
                    likesImg.classList.remove("liked");
                    likesCount.innerHTML = String(Number(likesCount.innerHTML) - 1);
                    await postFormData("/like?postId=" + ${post.getId()}, data, "delete");
                } else {
                    likesImg.classList.add("liked");
                    likesCount.innerHTML = String(Number(likesCount.innerHTML) + 1);
                    await postFormData("/like", data, "post");
                }
            }

            async function commentDelete(commentId) {
                let neededNode = document.querySelector('#comment' + commentId);
                const data = new URLSearchParams();
                data.append("id", commentId);
                neededNode.innerHTML = "";
                await postFormData("/comment?id=" + commentId, data, "delete");
            }

            function showCommentChangeForm(commentId) {
                let neededNode = document.querySelector('#comment' + commentId + '__content');

                let form = document.createElement("form");
                form.action = "/comment";
                form.method = "post";
                form.classList.add("comments__form_reply");
                form.innerHTML = `
                                <input type="text" id="text" name="text" class="comment__text comment__text_reply" placeholder="` + neededNode.firstElementChild.innerHTML + `" />
                                <input type="submit" value="Изменить">`

                neededNode.innerHTML = "";
                form.addEventListener("submit", replyHandlerChange);
                neededNode.appendChild(form);

            }

            function showReplyForm(commentId) {
                let neededNode = document.querySelector('#comment' + commentId + '>.comment__replies');
                let previousInnerHTML = neededNode.innerHTML.valueOf();
                neededNode.innerHTML = `
                       <div class="comment comment_reply">
                            <div class="comment__header comment__header_reply">
                                <img src="" alt="" class="comment__img comment__img_reply">
                                <p class="comment__name comment__name_reply">
                                  ${user.getName() + " " + user.getSurname()}
                                </p>
                            </div>
                            <div class="comment__content comment__content_reply">
                                <form action="/comment" method="post" class="comments__form_reply">
                                    <input type="text" id="text" name="text" class="comment__text comment__text_reply" />
                                    <input type="submit" value="Ответить">
                                </form>
                            </div>
                        </div>` + previousInnerHTML;
                let form = document.querySelector(".comments__form_reply");
                form.addEventListener("submit", replyHandler);
            }

            async function commentHandler(event) {
                event.preventDefault();

                const form = event.currentTarget;
                const url = form.action;

                try {
                    // const formData = new FormData(form);
                    // const responseData = await sendForm(url, formData);
                    const data = new URLSearchParams(new FormData(form));
                    console.log(document.querySelector(".textbox").value);
                    const responseData = await postFormData(url, data, "post");

                    let neededNode = document.querySelector(".comment__replies");

                    neededNode.innerHTML += `
                        <div class="comments__entity">
                            <div class="comment">
                                <div class="comment__header">
                                    <img src="" alt="" class="comment__img">
                                    <p class="comment__name">` + responseData['author']['name'] + " " + responseData['author']['surname'] +
                        `
                                    </p>
                                    <p class="comment__datetime">` + responseData['timestamp'] + `</p>
                                </div>
                                <div class="comment__content">
                                    <p class="comment__text">` + responseData['text'] + `</p>
                                    <div class="comment__default">
                                        <p class="comment__edit">изменить</p>
                                        <p class="comment__delete">удалить</p>
                                        <p class="comment__send">отправить</p>
                                    </div>
                                </div>
                            </div>
                        </div>`

                } catch (err) {
                    console.log(err);
                } finally {
                    form.reset();
                }
            }

            async function replyHandler(e) {
                e.preventDefault();

                const form = e.currentTarget;
                const url = form.action;


                try {
                    const data = new URLSearchParams(new FormData(form));
                    let answers = form.parentNode.parentNode.parentNode.parentNode.id.toString().substring(7).split("__").reverse().pop();
                    let post = ${post.getId()};
                    const responseData = await postFormData(url + "?answers=" + answers + "&post=" + post, data, "post");

                    let neededNode = form.parentNode;

                    neededNode.innerHTML = `<p class="comment__text comment__text_reply">` + responseData["text"] + `</p>
                                            <div class="comment__default comment__default_reply">
                                                    <p class="comment__edit comment__edit_reply">изменить</p>
                                                    <p class="comment__delete comment__delete_reply">удалить</p>
                                            </div>`;

                } catch (err) {
                    console.log(err);
                }
            }

            async function replyHandlerChange(e) {
                e.preventDefault();

                const form = e.currentTarget;
                const url = form.action;

                try {
                    const data = new URLSearchParams(new FormData(form));
                    let id = form.parentNode.id.toString().substring(7).split("__").reverse().pop();
                    let text = document.querySelector("#text").value;
                    // data.append("id", form.parentNode.id.toString().substring(7).split("__").reverse().pop());
                    let neededNode = form.parentNode;
                    neededNode.innerHTML = `<p class="comment__text comment__text_reply">` + text + `</p>`
                    const responseData = await postFormData(url + "?text=" + text + "&id=" + id, data, "put");


                } catch (err) {
                    console.log(err);
                }
            }

            async function postFormData(url, formData, method) {
                const response = await fetch(url, {
                    method: method,
                    body: formData
                });
                return await response.json();
            }

            async function sendForm(url, formData, method = "post") {
                const data = new URLSearchParams();
                for (const pair of formData) {
                    console.log(pair);
                    data.append(pair[0], pair[1]);
                }
                console.log(data.get("text"));
                return await postFormData(url, formData, method);
            }


            window.addEventListener("load", () => {
                document.querySelector(".comments__form").addEventListener("submit", commentHandler);
            });
        </script>
    </#if>
    <div class="left-part img">
        <div class="img__block" onmouseover="function showDesc() {
            let desc = document.querySelector('.description').style;
            desc.display = 'block';
            desc.position = 'absolute';
            desc.top = '0';
            desc.background = '#228b5e';
            desc.color = '#f0fff0';
            desc.margin = '20px';
            desc.padding = '20px';
            desc.borderRadius = '30px';
        }
        showDesc()" onmouseleave="function hideDesc() {
            let desc = document.querySelector('.description').style;
            desc.display = 'none';
        }
        hideDesc()">
            <img src="/files/images/posts/${post.getId()}/${post.getImage()?split("/")?last}" alt="" class="img__post">
            <div class="img__description description">
                <p class="description__text">${post.getDescription()}</p>
                <div class="description__default">
                    <#if user??>
                        <#if user.getId() == post.getAuthor().getId()>
                            <p class="description__change" onclick="showDescriptionChange()">изменить</p>
                            <p class="description__delete">удалить</p>
                        </#if>
                    </#if>
                </div>
            </div>
        </div>
    </div>
    <div class="right-part info">
        <div class="stats">
            <div class="likes">
                <#if isLiked>
                    <svg class="likes__img liked" xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                         viewBox="0 0 24 24">
                        <path d="M12 9.229c.234-1.12 1.547-6.229 5.382-6.229 2.22 0 4.618 1.551 4.618 5.003 0 3.907-3.627 8.47-10 12.629-6.373-4.159-10-8.722-10-12.629 0-3.484 2.369-5.005 4.577-5.005 3.923 0 5.145 5.126 5.423 6.231zm-12-1.226c0 4.068 3.06 9.481 12 14.997 8.94-5.516 12-10.929 12-14.997 0-7.962-9.648-9.028-12-3.737-2.338-5.262-12-4.27-12 3.737z"/>
                    </svg>
                <#else>
                    <svg class="likes__img" xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                         viewBox="0 0 24 24">
                        <path d="M12 9.229c.234-1.12 1.547-6.229 5.382-6.229 2.22 0 4.618 1.551 4.618 5.003 0 3.907-3.627 8.47-10 12.629-6.373-4.159-10-8.722-10-12.629 0-3.484 2.369-5.005 4.577-5.005 3.923 0 5.145 5.126 5.423 6.231zm-12-1.226c0 4.068 3.06 9.481 12 14.997 8.94-5.516 12-10.929 12-14.997 0-7.962-9.648-9.028-12-3.737-2.338-5.262-12-4.27-12 3.737z"/>
                    </svg>
                </#if>
                <p class="likes__count" onclick="like()">${likes}</p>
            </div>
            <div class="replies">
                <svg width="24" height="24" xmlns="http://www.w3.org/2000/svg" fill-rule="evenodd" clip-rule="evenodd">
                    <path d="M20 15c0 .552-.448 1-1 1s-1-.448-1-1 .448-1 1-1 1 .448 1 1m-3 0c0 .552-.448 1-1 1s-1-.448-1-1 .448-1 1-1 1 .448 1 1m-3 0c0 .552-.448 1-1 1s-1-.448-1-1 .448-1 1-1 1 .448 1 1m5.415 4.946c-1 .256-1.989.482-3.324.482-3.465 0-7.091-2.065-7.091-5.423 0-3.128 3.14-5.672 7-5.672 3.844 0 7 2.542 7 5.672 0 1.591-.646 2.527-1.481 3.527l.839 2.686-2.943-1.272zm-13.373-3.375l-4.389 1.896 1.256-4.012c-1.121-1.341-1.909-2.665-1.909-4.699 0-4.277 4.262-7.756 9.5-7.756 5.018 0 9.128 3.194 9.467 7.222-1.19-.566-2.551-.889-3.967-.889-4.199 0-8 2.797-8 6.672 0 .712.147 1.4.411 2.049-.953-.126-1.546-.272-2.369-.483m17.958-1.566c0-2.172-1.199-4.015-3.002-5.21l.002-.039c0-5.086-4.988-8.756-10.5-8.756-5.546 0-10.5 3.698-10.5 8.756 0 1.794.646 3.556 1.791 4.922l-1.744 5.572 6.078-2.625c.982.253 1.932.407 2.85.489 1.317 1.953 3.876 3.314 7.116 3.314 1.019 0 2.105-.135 3.242-.428l4.631 2-1.328-4.245c.871-1.042 1.364-2.384 1.364-3.75"/>
                </svg>
                <p class="replies__count">${comments?size}</p>
            </div>
        </div>
        <div class="comments">
            <div class="comments__add">
                <form action="/comment?post=${post.getId()}" method="post" class="comments__form">
                    <input class="textbox" type="text" name="text" placeholder="Введите комментарий"/>
                    <input type="submit" value="Добавить"/>
                </form>
            </div>
            <#list comments?filter(c -> !c.answerTo??) as comment>
                <div class="comments__entity">
                    <div class="comment" id="comment${comment.getId()}">
                        <div class="comment__header">
                            <img src="" alt="" class="comment__img">
                            <#assign author = comment.getAuthor()>
                            <p class="comment__name">
                                <@fullname author/>
                            </p>
                            <p class="comment__datetime">${comment.getTimestamp()}</p>
                        </div>
                        <div class="comment${comment.getId()}__content" id="comment${comment.getId()}__content">
                            <p class="comment__text">${comment.getText()}</p>
                            <div class="comment__default">
                                <#if user??>
                                    <p class="comment__reply" onclick="
                                            showReplyForm(${comment.getId()})">ответить</p>
                                    <#if comment.getAuthor().getId() == user.getId()>
                                        <p class="comment__edit" onclick="showCommentChangeForm(${comment.getId()})">
                                            изменить</p>
                                        <p class="comment__delete" onclick="commentDelete(${comment.getId()})">
                                            удалить</p>
                                    </#if>
                                </#if>
                            </div>
                        </div>
                        <div class="comment__replies">
                            <#list comments?filter(c -> c.answerTo?? && c.answerTo.id == comment.id) as reply>
                                <div class="comment comment_reply" id="comment${reply.getId()}">
                                    <div class="comment__header comment__header_reply">
                                        <img src="" alt="" class="comment__img comment__img_reply">
                                        <#assign authorReply = reply.getAuthor()>
                                        <p class="comment__name comment__name_reply">
                                            <@fullname authorReply/>
                                        </p>
                                        <p class="comment__datetime comment__datetime_reply">${reply.getTimestamp()}</p>
                                    </div>
                                    <div class="comment${reply.getId()}__content comment__content_reply"
                                         id="comment${reply.getId()}__content">
                                        <p class="comment__text comment__text_reply">${reply.getText()}</p>
                                        <div class="comment__default comment__default_reply">
                                            <#if user?? && reply.getAuthor().getId() == user.getId()>
                                                <p class="comment__edit comment__edit_reply"
                                                   onclick="showCommentChangeForm(${reply.getId()})">изменить</p>
                                                <p class="comment__delete comment__delete_reply"
                                                   onclick="commentDelete(${reply.getId()})">удалить</p>
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
        <div class="info__default">
            <p class="info__report">пожаловаться/</p>
            <p class="info__delete">удалить</p>
        </div>
    </div>
</@>
