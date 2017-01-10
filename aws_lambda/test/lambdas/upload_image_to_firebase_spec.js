'use strict';
import {handler} from "../../lambdas/upload_image_to_firebase";
import {config} from "../../config/firebase";
import * as firebase from "firebase";
import sinon from "sinon";
import {expect} from "../testHelper";
import * as resizeModule from "../../modules/resize_image";

const event = {
    format: "png",
    imageFile: "i am awesome ajae gag",
    width: 720,
    height: 1280
};

const TIMESTAMP_2017_06_19 = 1497830400000;

const stubFirebaseApp = {
    storage: () => {}
};

const stubFireBaseStorage = {
    ref: () => {}
};

const stubFirebaseRef = {
    child: () => {},
    put: () => {}
};

const stubUploadTask = {
    on: () => {}
};

describe("upload image to firebase", () => {
    let firebase_initializeAppStub,
        firebaseApp_storageStub,
        firebaseRef_childStub,
        firebaseRef_putStub,
        firebaseStorage_refStub;

    beforeEach(() => {
        sinon.useFakeTimers(TIMESTAMP_2017_06_19);

        firebase_initializeAppStub = sinon.stub(firebase, "initializeApp");
        firebaseApp_storageStub = sinon.stub(stubFirebaseApp, "storage");
        firebaseStorage_refStub = sinon.stub(stubFireBaseStorage, "ref");
        firebaseRef_childStub = sinon.stub(stubFirebaseRef, "child");
        firebaseRef_putStub = sinon.stub(stubFirebaseRef, "put");

        firebase_initializeAppStub.returns(stubFirebaseApp);
        firebaseApp_storageStub.returns(stubFireBaseStorage);
        firebaseStorage_refStub.returns(stubFirebaseRef);
        firebaseRef_childStub.returns(stubFirebaseRef);
        firebaseRef_putStub.returns(stubUploadTask);
    });

    afterEach(() => {
        firebase.initializeApp.restore();
        stubFirebaseApp.storage.restore();
        stubFireBaseStorage.ref.restore();
        stubFirebaseRef.child.restore();
        stubFirebaseRef.put.restore();
    });

    it("initializes firebase with config", () => {
        handler(event, {}, sinon.spy());

        sinon.assert.calledWith(firebase_initializeAppStub, config);
    });

    it("gets storage from firebase app", () => {
        handler(event, {}, sinon.spy());

        sinon.assert.called(firebaseApp_storageStub);
    });

    it("gets reference object from storage", () => {
        handler(event, {}, sinon.spy());

        sinon.assert.called(firebaseStorage_refStub);
    });

    it("gets child from firebase storage reference", () => {
        handler(event, {}, sinon.spy());

        sinon.assert.calledWith(firebaseRef_childStub, `gags/${TIMESTAMP_2017_06_19}`);
    });

    describe("after resizing image", () => {
        beforeEach(() => {
            sinon.stub(stubUploadTask, "on");
        });

        afterEach(() => {
            resizeModule.resizeImage.restore();
            stubUploadTask.on.restore();
        });

        it("resizes image with arguments", () => {
            sinon.stub(resizeModule, "resizeImage").returns(Promise.resolve("whatever"));

            handler(event, {}, sinon.spy());

            expect(resizeModule.resizeImage.calledWith("png", "i am awesome ajae gag", 720, 1280))
                .to.be.true;
        });

        it("puts blob to firebase image reference", (done) => {
            let resizePromise = Promise.resolve("i am a file");

            sinon.stub(resizeModule, "resizeImage").returns(resizePromise);

            handler(event, {}, sinon.spy());

            resizePromise.then(() => {
                sinon.assert.calledWith(stubFirebaseRef.put, "i am a file");

                done();
            }).catch(done);
        });

        it("after putting blob, add state_changed listener", (done) => {
            let resizePromise = Promise.resolve("i am a file");
            let addUploadHandlerPromise = Promise.resolve(stubUploadTask);

            sinon.stub(resizeModule, "resizeImage").returns(resizePromise);

            handler(event, {}, sinon.spy());

            resizePromise.then(() => {
                return addUploadHandlerPromise;
            }).then((stubUploadTask) => {
                sinon.assert.calledWith(stubUploadTask.on, "state_changed");

                done();
            }).catch(done);
        });

        it("after completion of upload, response to client with download url", (done) => {
            let resizePromise = Promise.resolve("i am a file");
            let addUploadHandlerPromise = Promise.resolve(stubUploadTask);

            sinon.stub(resizeModule, "resizeImage").returns(resizePromise);

            let context = { succeed: () => {}};
            let context_succeed = sinon.spy(context, "succeed");

            handler(event, context, sinon.spy());

            resizePromise.then(() => {
                return addUploadHandlerPromise;
            }).then((stubUploadTask) => {
                let completeCallback = stubUploadTask.on.getCall(0).args[3];

                stubUploadTask["snapshot"] = {
                    downloadURL: "my awesome url"
                };

                completeCallback();

                sinon.assert.calledWith(context_succeed, {
                    status: 200,
                    downloadURL: "my awesome url"
                });

                done();
            }).catch(done);
        });
    });
});