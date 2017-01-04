let path = require("path"),
    fs = require("fs");

module.exports = {
    entry: fs.readdirSync(path.join(__dirname, "./lambdas"))
        .filter(fileName => /\.js$/.test(fileName))
        .map(fileName => {
            let entry = {};
            entry[fileName.replace(".js", "")] = path.join(
                __dirname,
                "./lambdas/",
                fileName
            );

            return entry;
        })
        .reduce((finalObject, entry) => Object.assign(finalObject, entry)),
    output: {
        path: path.join(__dirname, "dist"),
        library: "[name]",
        libraryTarget: "commonjs2",
        filename: "[name].js"
    },
    target: "node",
    module: {
        loaders: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel',
                query: {
                    presets: ['es2015']
                }
            }
        ]
    }
};