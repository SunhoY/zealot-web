var path = require('path'),
    fs = require("fs"),
    webpack = require("webpack");

module.exports = {
    entry: fs.readdirSync(path.join(__dirname, "./lambdas"))
        .filter(fileName => /\.js$/.test(fileName))
        .map(fileName => {
            var entry = {};
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
    externals: {
        "imagemagick": "imagemagick"
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
            },
            {
                test: /\.json$/,
                loader: 'json'
            }
        ]
    },
    plugins: [
        new webpack.IgnorePlugin(/vertx/),
        new webpack.optimize.UglifyJsPlugin()
    ]
};