import React, { Component } from 'react';
import { render } from 'react-dom';
import OntoReact from '../../src';

class Demo extends Component {
    state = { loading: false };

    uc1_condition = {
        classUri: 'http://drozdowicz.net/onto/access-control#PermittedRequest',
        propertyConditions: {
            type: 'intersection',
            contents: [
                {
                    type: 'condition',
                    propertyUri: 'http://drozdowicz.net/onto/access-control#requestedBy',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri: 'http://xmlns.com/foaf/0.1/Person',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri: 'http://www.w3.org/ns/org#memberOf',
                            operator: 'equalToIndividual',
                            objectValue: 'http://drozdowicz.net/onto/privacy-sample#healthCenter'
                        }
                    }
                },
                {
                    type: 'condition',
                    propertyUri: 'http://drozdowicz.net/onto/access-control#concernsResource',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri: 'http://drozdowicz.net/onto/fitness-tracking#AggregateMetric',
                        propertyConditions: {
                            type: 'intersection',
                            contents: [
                                {
                                    type: 'condition',
                                    propertyUri: 'http://drozdowicz.net/onto/fitness-tracking#aggregationDays',
                                    operator: 'greaterThan',
                                    datatypeValue: '30'
                                },
                                {
                                    type: 'condition',
                                    propertyUri: 'http://drozdowicz.net/onto/fitness-tracking#aggregatesMetric',
                                    operator: 'constrainedBy',
                                    classConstraintValue: {
                                        classUri: 'http://drozdowicz.net/onto/fitness-tracking#Distance',
                                        propertyConditions: [
                                            {}
                                        ]
                                    }
                                }
                            ]
                        }
                    }
                }
            ]
        }        
    };

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
                <h1>Privacy Sample - UC1</h1>
                {React.createElement(OntoReact.OntoReact, {
                    mainClass: 'http://drozdowicz.net/onto/access-control#PermittedRequest',
                    //api: false,
                    elementName: 'HealthCenterPermission',
                    isDescriptionOfIndividual: false,
                    headerComponent: OntoReact.InputNameHeader('Class name'),
                    // headerComponent: OntoReact.MultiHeader(
                    //     OntoReact.InputNameHeader('Class name'),
                    //     OntoReact.SelectClassHeader('Subclass of:')
                    // ),
                    api: new OntoReact.Api(false),
                    title:
                        'Add new class expression for PermittedRequest',
                    condition: this.uc1_condition,
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
