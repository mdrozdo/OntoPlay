import React, { Component } from 'react';
import { render } from 'react-dom';
import OntoReact from '../../src';

class Demo extends Component {
    state = { loading: false };

    uc1_condition = {
        classUri: 'https://w3id.org/sxacml/access-control#PermittedRequest',
        propertyConditions: {
            type: 'intersection',
            contents: [
                {
                    type: 'condition',
                    propertyUri: 'https://w3id.org/sxacml/access-control#requestedBy',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri: 'http://xmlns.com/foaf/0.1/Person',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri: 'http://www.w3.org/ns/org#memberOf',
                            operator: 'equalToIndividual',
                            valueClassUri: 'http://www.w3.org/ns/org#Organization',
                            individualValue: 'https://w3id.org/sxacml/sample-privacy/privacy-mapping#healthCenter'
                        }
                    }
                },
                {
                    type: 'condition',
                    propertyUri: 'https://w3id.org/sxacml/access-control#concernsResource',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri: 'https://w3id.org/sxacml/sample-privacy/fitness-tracking#AggregateMetric',
                        propertyConditions: {
                            type: 'intersection',
                            contents: [
                                {
                                    type: 'condition',
                                    propertyUri: 'https://w3id.org/sxacml/sample-privacy/fitness-tracking#aggregationDays',
                                    operator: 'greaterThan',
                                    datatypeValue: '30'
                                },
                                {
                                    type: 'condition',
                                    propertyUri: 'https://w3id.org/sxacml/sample-privacy/fitness-tracking#aggregatesMetric',
                                    operator: 'constrainedBy',
                                    classConstraintValue: {
                                        classUri: 'https://w3id.org/sxacml/sample-privacy/fitness-tracking#Distance',
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

    uc2_condition = {
        'classUri': 'https://w3id.org/sxacml/access-control#PermittedRequest',
        'propertyConditions': {
            'type': 'intersection',
            'contents': [
                {
                    'type': 'condition',
                    'propertyUri': 'https://w3id.org/sxacml/access-control#concernsResource',
                    'operator': 'constrainedBy',
                    'classConstraintValue': {
                        'classUri': 'https://w3id.org/sxacml/sample-privacy/fitness-tracking#OutdoorTraining',
                        'propertyConditions': [
                            {}
                        ]
                    }
                },
                {
                    'type': 'condition',
                    'propertyUri': 'https://w3id.org/sxacml/access-control#concernsAction',
                    'operator': 'constrainedBy',
                    'classConstraintValue': {
                        'classUri': 'https://w3id.org/sxacml/access-control#Read',
                        'propertyConditions': {
                            'type': 'condition',
                            'propertyUri': 'https://w3id.org/sxacml/access-control#requestedForPurpose',
                            'operator': 'constrainedBy',
                            'classConstraintValue': {
                                'classUri': 'https://w3id.org/sxacml/sample-privacy/privacy-mapping#InfrastructureAnalysis',
                                'propertyConditions': [
                                    {}
                                ]
                            }
                        }
                    }
                }
            ]
        }
    };

    render() {
        return (
            <div className='container'>
                <h1>Privacy Sample - UC1</h1>
                {React.createElement(OntoReact.OntoReact, {
                    mainClass: 'https://w3id.org/sxacml/access-control#PermittedRequest',
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

                <h1>Privacy Sample - UC2</h1>
                {React.createElement(OntoReact.OntoReact, {
                    mainClass: 'https://w3id.org/sxacml/access-control#PermittedRequest',
                    //api: false,
                    elementName: 'CyclingRoutePermission',
                    isDescriptionOfIndividual: false,
                    headerComponent: OntoReact.InputNameHeader('Class name'),
                    // headerComponent: OntoReact.MultiHeader(
                    //     OntoReact.InputNameHeader('Class name'),
                    //     OntoReact.SelectClassHeader('Subclass of:')
                    // ),
                    api: new OntoReact.Api(false),
                    title:
                        'Add new class expression for PermittedRequest',
                    condition: this.uc2_condition,
                })}

            </div>
        );
    }
}

render(<Demo />, document.querySelector('#demo'));
