import React from 'react';
import Landing from '../../app/component/Landing';
import renderer from 'react-test-renderer';

describe('Landing Test', () => {
    let subject,
        subjectTree;

    beforeEach(() => {
        subject = renderer.create(
            <Landing />
        );

        subjectTree = subject.toJSON();
    });

    it("renders correctly", () => {
        expect(subjectTree).toMatchSnapshot();
    });
});
