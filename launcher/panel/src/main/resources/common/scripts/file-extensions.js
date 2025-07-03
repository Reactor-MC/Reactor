const allowedExtensions = ['yml', 'json', 'xml', 'toml', 'image', 'java', 'js', 'txt'];
const aliases = new Map([
    ["yml", "yaml"],
    ["html", "xml"],
    ["png", "image"],
    ["jpg", "image"],
    ["webp", "image"],
    ["jar", "java"],
    ["class", "java"]
]);

export function GetAlias(name) {
    return aliases[name];
}

export function SupportExtension(name) {
    return allowedExtensions.includes(name);
}