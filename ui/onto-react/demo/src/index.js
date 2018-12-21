import React, { Component } from 'react';
import { render } from 'react-dom';
import OntoReact from '../../src';

class Demo extends Component {
    state = { loading: false };

    fullCondition = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'union',
            contents: [
                {
                    type: 'condition',
                    propertyUri:
                        'http://gridagents.sourceforge.net/AiGGridOntology#hasGPU',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri:
                            'http://gridagents.sourceforge.net/AiGGridOntology#GPU',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri:
                                'http://gridagents.sourceforge.net/AiGGridOntology#hasArchitecture',
                            operator: 'constrainedBy',
                            classConstraintValue: {
                                classUri:
                                    'http://gridagents.sourceforge.net/AiGGridOntology#CPUArchictecture',
                                propertyConditions: {
                                    type: 'condition',
                                    propertyUri:
                                        'http://purl.org/NET/cgo#hasName',
                                    operator: 'equalTo',
                                    datatypeValue: 'PowerPC',
                                    annotations: [],
                                },
                                annotations: [],
                            },
                            annotations: [],
                        },
                        annotations: [],
                    },
                    annotations: [],
                },
                {
                    type: 'intersection',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'dupa',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '12',
                            annotations: [],
                        },
                    ],
                },
            ],
        },
    };

    emptyCondition = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {},
    };

    simpleCondition = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'condition',
            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
            operator: 'equalTo',
            datatypeValue: '12',
            annotations: [],
        },
    };

    intersectionCondition = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'intersection',
            contents: [
                {
                    type: 'condition',
                    propertyUri: 'http://purl.org/NET/cgo#hasName',
                    operator: 'equalTo',
                    datatypeValue: 'dupa',
                    annotations: [],
                },
                {
                    type: 'condition',
                    propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                    operator: 'equalTo',
                    datatypeValue: '12',
                    annotations: [],
                },
            ],
        },
    };

    unionCondition = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'union',
            contents: [
                {
                    type: 'condition',
                    propertyUri: 'http://purl.org/NET/cgo#hasName',
                    operator: 'equalTo',
                    datatypeValue: 'dupa',
                    annotations: [],
                },
                {
                    type: 'condition',
                    propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                    operator: 'equalTo',
                    datatypeValue: '12',
                    annotations: [],
                },
            ],
        },
    };

    unionOfIntersections = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'union',
            contents: [
                {
                    type: 'intersection',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'dupa',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '12',
                            annotations: [],
                        },
                    ],
                },
                {
                    type: 'intersection',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'asdasda',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '56454',
                            annotations: [],
                        },
                    ],
                },
            ],
        },
    };

    intersectionOfUnions = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'intersection',
            contents: [
                {
                    type: 'union',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'dupa',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '12',
                            annotations: [],
                        },
                    ],
                },
                {
                    type: 'union',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'asdasda',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '56454',
                            annotations: [],
                        },
                    ],
                },
            ],
        },
    };

    intersectionOfIntersections = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'intersection',
            contents: [
                {
                    type: 'intersection',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'dupa',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '12',
                            annotations: [],
                        },
                    ],
                },
                {
                    type: 'intersection',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'asdasda',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '56454',
                            annotations: [],
                        },
                    ],
                },
            ],
        },
    };

    unionOfUnions = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'union',
            contents: [
                {
                    type: 'union',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'dupa',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '12',
                            annotations: [],
                        },
                    ],
                },
                {
                    type: 'union',
                    contents: [
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#hasName',
                            operator: 'equalTo',
                            datatypeValue: 'asdasda',
                            annotations: [],
                        },
                        {
                            type: 'condition',
                            propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                            operator: 'equalTo',
                            datatypeValue: '56454',
                            annotations: [],
                        },
                    ],
                },
            ],
        },
    };

    individualCondition = {
        classUri: 'http://purl.org/NET/cgo#WorkerNode',
        propertyConditions: {
            type: 'values',
            contents: [
                {
                    type: 'condition',
                    propertyUri: 'http://purl.org/NET/cgo#hasName',
                    operator: 'equalTo',
                    datatypeValue: 'dupa',
                    annotations: [],
                },
                {
                    type: 'condition',
                    propertyUri: 'http://purl.org/NET/cgo#waitingJobs',
                    operator: 'equalTo',
                    datatypeValue: '12',
                    annotations: [],
                },
            ],
        },
    };

    render() {
        return (
            <div className='container'>
                <h1>onto-react Demo - Individual</h1>
                {React.createElement(OntoReact.OntoReact, {
                    mainClass: 'http://purl.org/NET/cgo#WorkerNode',
                    //api: false,
                    elementName: 'http://purl.org/NET/cgo#WorkerNode',
                    isDescriptionOfIndividual: true,
                    // headerComponent: OntoReact.InputNameHeader('Individual name'),
                    headerComponent: OntoReact.MultiHeader(
                        OntoReact.InputNameHeader('Individual name'),
                        OntoReact.SelectClassHeader('Of class:')
                    ),
                    api: new OntoReact.Api(true),
                    title:
                        'Add new individual of class http://purl.org/NET/cgo#WorkerNode',
                    condition: this.individualCondition,
                })}

                <h1>onto-react Demo - class</h1>
                {React.createElement(OntoReact.OntoReact, {
                    mainClass: 'http://purl.org/NET/cgo#WorkerNode',
                    //api: false,
                    elementName: 'http://purl.org/NET/cgo#WorkerNode',
                    classRelation: 'SUBCLASS',
                    // headerComponent: OntoReact.InputNameHeader('Individual name'),
                    headerComponent: OntoReact.MultiHeader(
                        OntoReact.InputNameHeader('Individual name'),
                        OntoReact.SelectClassHeader('Map class to:')
                    ),
                    api: new OntoReact.Api(false),
                    title:
                        'Add new class mapping for http://purl.org/NET/cgo#WorkerNode',
                    condition: this.intersectionCondition,
                })}
            </div>
        );
    }
}

render(<Demo />, document.querySelector('#demo'));
