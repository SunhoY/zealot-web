var path = require('path');
var webpack = require('webpack');

module.exports = {
    context: __dirname + "/app",
    entry: {
        javascript: './index.js',
        html: "./index.html",
    },
    resolve: {
        extensions: ['', '.js'],
        root: path.resolve(__dirname, "./app")
    },
    output: {
        filename: 'bundle.js',
        path: __dirname + "/dist"
    },
    module: {
        preLoaders: [
            {
                test: /\.json$/,
                loader: 'json'
            }
        ],
        loaders: [
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                loaders: ["react-hot", "babel-loader"]
            },
            {
                test: /\.html$/,
                loader: "file?name=[name].[ext]"
            },
            {
                test: /\.png$/,
                loader: "url-loader?mimetype=image/png"
            },
            {
                test: /\.css$/,
                loader: "style-loader!css-loader"
            },
            {
                test: /\.ttf$/,
                loader: "file?name=assets/font/[name].[ext]"
            }
        ]
    }
};