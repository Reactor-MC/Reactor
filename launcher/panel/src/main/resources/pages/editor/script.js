let editor;

async function loadFileContent() {
    const codeContainer = document.getElementById('code-container');
    const urlParams = new URLSearchParams(window.location.search);
    const pathParam = urlParams.get('path');

    if (pathParam == null) {
        codeContainer.innerHTML = `<p class="panel-text">Invalid path</p>`
        return;
    }

    loadFilePath(pathParam);

    try {
        const response = await fetch(`/api/download/?path=${pathParam}`);
        if (!response.ok) throw new Error('HTTP code ' + response.status);

        const buffer = await response.arrayBuffer();
        var enc = new TextDecoder();

        const lastDotIndex = pathParam.lastIndexOf('.');
        editor = ace.edit(codeContainer);
        if (lastDotIndex > 0) {
            editor.session.setMode("ace/mode/" + pathParam.substring(lastDotIndex + 1).toLowerCase());
        }
        editor.setTheme("ace/theme/chaos");
        editor.setOptions({
            enableBasicAutocompletion: true,
            enableLiveAutocompletion: true,
            fontSize: "14px"
        });

        editor.setValue(enc.decode(buffer));
    } catch (error) {
        codeContainer.innerHTML = `<p class="panel-text">${error}</p>`
        console.error(error);
        return;
    }
}

function loadFilePath(path) {
    const split = path.split("/");
    const fragment = document.createDocumentFragment();

    let destination = "/";
    for (let i = 1; i < split.length-1; i++) {
        const a = document.createElement('a');
        a.className = 'panel-text';
        a.innerHTML = split[i] + '/';
        destination += split[i] + '/';
        a.setAttribute("href", "/pages/file/?path=" + destination);
        fragment.appendChild(a);
    }

    const fileName = document.createElement('h1');
    fileName.className = 'panel-text'
    fileName.innerHTML = split[split.length-1];
    fragment.appendChild(fileName);

    document.getElementById('file-path').appendChild(fragment);
}

loadFileContent();

document.getElementById("save-button").onclick = handleSaveButton;

function handleSaveButton() {
    if (editor == null) {
        return;
    }
    const urlParams = new URLSearchParams(window.location.search);
    const pathParam = urlParams.get('path');
    if (pathParam == null) {
        return;
    }

    const saveScreen = document.getElementById("save-screen");
    saveScreen.removeAttribute("hidden");

    saveScreen.innerHTML = `<p class="panel-text">Saving</p>`

    saveContent().then((error) => {
        if (error == null) {
            saveScreen.setAttribute("hidden", "true");
            return;
        }
        saveScreen.innerHTML = `<p class="panel-text">${error}</p>`;
        setTimeout(() => saveScreen.setAttribute("hidden", "true"), 2000);
    });
}

function saveContent() {
    return new Promise(async resolve => {
        if (editor == null) {
            resolve("Can't load editor");
            return;
        }

        try {
            await fetch()
            throw new Error("a");
            resolve(null);
        } catch (error) {
            console.error(error);
            resolve(error);
        }
    })
}