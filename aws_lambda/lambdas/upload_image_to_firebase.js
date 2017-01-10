'use strict';

import * as firebase from "firebase";
import {config} from "../config/firebase";
import {resizeImage} from "../modules/resize_image";

function handler(event, context, callback) {
    let firebaseApp = firebase.initializeApp(config);

    const {format, imageFile, width, height} = event;
    var timestamp = new Date().getTime();

    let storage = firebaseApp.storage();
    let storageReference = storage.ref();
    let imageReference = storageReference.child(`gags/${timestamp}`);

    resizeImage(format, imageFile, width, height)
        .then((file) => imageReference.put(file))
        .then((uploadTask) => {
            uploadTask.on("state_changed", () => { }, () => {}, () => {
                context.succeed({
                    status: 200,
                    downloadURL: uploadTask.snapshot.downloadURL
                });
            });
    });
}

export { handler };
