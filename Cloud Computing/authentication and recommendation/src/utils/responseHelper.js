exports.success = (h, message, data, code = 200) => h.response({ message, data }).code(code);
exports.error = (h, message, error, code = 500) => h.response({ message, error }).code(code);
