import { GetAlias, SupportExtension } from "/common/scripts/file-extensions.js"	

async function loadFiles() {
    const filesContainer = document.getElementById('files-container');
    const urlParams = new URLSearchParams(window.location.search);
    const pathParam = urlParams.get('path') || "";

    let filesResponse;
    try {   
        filesResponse = await fetch(`/api/files/?path=${pathParam}`);
        if (!filesResponse.ok) throw new Error('HTTP code ' + filesResponse.status);
    } catch (error) {
        filesContainer.innerHTML = `<p class="panel-text">${error}</p>`
        console.error(error);
        return;
    }

    const files = await filesResponse.json();
    const fragment = document.createDocumentFragment();

    for (const file of files) {
        await createFile(file, fragment, pathParam);
    }

    filesContainer.appendChild(fragment);

    if (files.length === 0) {
        filesContainer.innerHTML = `<p class="panel-text">No files found</p>`
    }
}

async function createFile(file, fragment, currentPath) {
    const div = document.createElement('div');
    div.className = 'file-item panel-div';

    const date = new Date(file.modifiedTime * 1000);
    const dayMonthYear = date.toLocaleString('default', {
        day: '2-digit',
        month: 'long',
        year: 'numeric'
    });           
    const time = date.toLocaleString('default', {
        hour: 'numeric',
        minute: 'numeric',
        hour12: false
    });

    const imagePath = await getImagePath(file.fileName, file.weight);
    const redirectTo = file.weight == -1
        ? "/pages/file/?path=" + currentPath + "/" + file.fileName
        : "/pages/editor/?path=" + currentPath + "/" + file.fileName;

    div.innerHTML = `
        <input type="checkbox">
        <img src="${imagePath}" alt="${file.fileName}" onerror="this.src='icons/default.svg'">
        <a href="${redirectTo}" class="file-name panel-text">${file.fileName}</a>
        <div class="file-meta">
            <span class="panel-span">${file.weight < 0 ? '' : formatBytes(file.weight)}</span>
            <span class="panel-span">${dayMonthYear}</span>
            <span class="panel-span">${time}</span>
        </div>
        <button class="file-options panel-button">⋮</button>`;

    fragment.appendChild(div);
}

async function getImagePath(filename, weight) {
    if (weight == -1) {
        return 'icons/folder.svg';
    }

    if (filename.startsWith('.') && filename.indexOf('.', 1) === -1) {
        return 'icons/default.svg';
    }

    const lastDotIndex = filename.lastIndexOf('.');
    if (lastDotIndex <= 0) {
        return 'icons/default.svg';
    }

    let extension = filename.substring(lastDotIndex + 1).toLowerCase();

    const foundAlias = GetAlias(extension);
    if (foundAlias != null) {
        extension = foundAlias;
    }

    if (!SupportExtension(extension)) {
        return 'icons/default.svg';
    }

    return 'icons/' + extension + '.svg';
}


function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';
  
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
  
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
  
    const value = bytes / Math.pow(k, i);
    return parseFloat(value.toFixed(dm)) + ' ' + sizes[i];
}

loadFiles();