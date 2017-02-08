var path = require('path');
var webpack = require('webpack');

module.exports = {
    context: __dirname + "/app",
    entry: [
        './index.js',
        "./index.html"
    ],
    resolve: {
        extensions: ['', '.js'],
        root: path.resolve(__dirname, "./app")
    },
    output: {
        filename: 'bundle.js',
        path: __dirname + "/dist"
    },
    target: "node",
    module: {
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
            },
            {
                test: /\.json$/,
                loader: 'json'
            }
        ]
    },
    plugins: [
        new webpack.DefinePlugin({
            "process.env": {
                NODE_ENV: JSON.stringify("production")
            }
        })
    ]
};