'use strict';

import imagemagick from "imagemagick";
import fs from "fs";

const MAX_WIDTH = 720;

function calculateSize(width, height) {
    if (width <= MAX_WIDTH) {
        return {
            width: width,
            height: height
        };
    }

    let ratio = width / MAX_WIDTH;
    let newWidth = MAX_WIDTH;
    let newHeight = height / ratio;

    return {width: newWidth, height: newHeight};
}

function resizeImage(format, imageFile, width, height) {
    let newSize = calculateSize(width, height);
    let dstPath = "/temp/ajae";

    return new Promise((resolve, reject) => {
        imagemagick.resize({
            dstPath: dstPath,
            srcData: new Buffer(imageFile, "base64"),
            quality: 1.0,
            width: newSize.width,
            height: newSize.height,
            format: format
        }, (err) => {
            if (err) {
                reject(err);
            }
            else {
                let resultFile = new Buffer(fs.readFileSync("/temp/ajae")).toString();
                fs.unlinkSync("/temp/ajae");

                resolve(resultFile);
            }
        });
    });
}

export {resizeImage};