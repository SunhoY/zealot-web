import imagemagick from 'imagemagick';
import sinon from 'sinon';
import {resizeImage} from "../../modules/resize_image";
import {expect, assert} from "../testHelper";
import fs from "fs";

describe("resize image spec", () => {
    let resizeOption,
        resizeCallback,
        result;

    beforeEach(() => {
        sinon.stub(imagemagick, "resize");

        result = resizeImage("png", "some base 64 encoded image", 1080, 1920);

        resizeOption = imagemagick.resize.getCall(0).args[0];
        resizeCallback = imagemagick.resize.getCall(0).args[1];
    });

    afterEach(() => {
        imagemagick.resize.restore();
    });

    describe("resize option test", () => {
        it("has srcPath as /temp/ajae", () => {
            expect(resizeOption.dstPath).not.to.be.undefined;
            expect(resizeOption.dstPath).to.be.equal("/temp/ajae");
        });

        it("has srcData as buffer with binary", () => {
            expect(resizeOption.srcData).not.to.be.undefined;
            expect(resizeOption.srcData).to.deep.equal(new Buffer("some base 64 encoded image", "base64"));
        });

        it("has width as resized width", () => {
            expect(resizeOption.width).to.be.equal(720);
            expect(resizeOption.height).to.be.equal(1280);
        });

        it("has format as extension", () => {
            expect(resizeOption.format).to.be.equal("png");
        });
    });

    describe("resize callback", () => {
        let readFileSyncStub;

        beforeEach(() => {
            readFileSyncStub = sinon.stub(fs, "readFileSync");
            sinon.stub(fs, "unlinkSync");
        });

        afterEach(() => {
            fs.readFileSync.restore();
            fs.unlinkSync.restore();
        });

        it("calls reject on error", (done) => {
            result.catch(() => {
                done();
            });

            resizeCallback("i am an error");
        });

        it("calls resolve on success", (done) => {
            readFileSyncStub.returns("whatever");
            result.then(() => {
                done();
            });

            resizeCallback();
        });

        it("resolves with file binary string", (done) => {
            readFileSyncStub.withArgs("/temp/ajae").returns("i am binary you want");

            result.then((imageFile) => {
                assert.equal(imageFile, "i am binary you want");
                done();
            }).catch(done);

            resizeCallback();
        });

        it("unlinks resized ajae file", (done) => {
            readFileSyncStub.returns("whatever");
            result.then(() => {
                expect(fs.unlinkSync.calledWith("/temp/ajae")).to.be.true;
                done();
            }).catch(done);

            resizeCallback();
        });
    });
});