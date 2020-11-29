const CompressionWebpackPlugin = require('compression-webpack-plugin');
module.exports = {
    productionSourceMap: process.env.NODE_ENV != 'production',
    configureWebpack: (config) => {
        config.plugins.push(
            new CompressionWebpackPlugin({
                algorithm: 'gzip',
                test: /\.js$|\.html$|\.json$|\.css/,
                threshold: 10240,
                minRatio: 0.8,
            })
        );
    },
};