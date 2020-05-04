/**
 * Bindings to use mustache.js with Nashorn.
 */

// This function will be called during script execution.
// This is a simple proxy to mustache implementation.
function render(template, view, partials) {
	return Mustache.render(template, view, partials);
}