/**
 * Bindings to use mustache.js with Nashorn.
 */

// Render mustache partials.
// This function will be called during script execution.
function render(template, view, partials) {
  return Mustache.render(template, view, partials);
}