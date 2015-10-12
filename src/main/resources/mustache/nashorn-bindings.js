/**
 * Bindings to use mustache.js with Nashorn.
 */

// Use Nashorn Template Loader.
// This variable is set during script engine creation.
function $loadPartial(name) {
  return $loader.load(name);
}

// Render mustache partials.
// This function will be called during script execution.
function render(template, view) {
  return Mustache.render(template, view, $loadPartial);
}