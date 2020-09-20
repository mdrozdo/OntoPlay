import React, { Component } from 'react';
import { render } from 'react-dom';
import OntoReact from '../../src';

class Demo extends Component {
    state = { loading: false };

    fullCondition = {
        classUri: 'https://w3id.org/sxacml/sample-port/port#Driver',
        propertyConditions: {
            type: 'condition',
            propertyUri: 'https://w3id.org/sxacml/sample-port/port#employedBy',
            operator: 'constrainedBy',
            classConstraintValue: {
                classUri: 'http://ontology.tno.nl/transport#TransportParty',
                propertyConditions: {
                    type: 'condition',
                    propertyUri:
                        'https://w3id.org/sxacml/sample-port/port#ownsEquipment',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri:
                            'https://w3id.org/sxacml/sample-port/port#ObservedTruck',
                        propertyConditions: [{}],
                    },
                },
            },
        },
    };

    indivCondition = {
        classUri: 'https://w3id.org/sxacml/sample-port/port#Driver',
        propertyConditions: {
            type: 'values',
            contents: [
                {
                    type: 'condition',
                    propertyUri:
                        'https://w3id.org/sxacml/sample-port/port#employedBy',
                    operator: 'equalToIndividual',
                    valueClassUri: 'http://ontology.tno.nl/logico#Carrier',
                    individualValue:
                        'https://w3id.org/sxacml/sample-port/port-data#GlobexCorp',
                },
                {
                    type: 'condition',
                    propertyUri: 'http://ontology.tno.nl/logico#hasEmail',
                    operator: 'equalTo',
                    datatypeValue: 'clarkcontoso@globex.com',
                },
                {
                    type: 'condition',
                    propertyUri:
                        'https://w3id.org/sxacml/sample-port/port#drivesTruck',
                    operator: 'describedWith',
                    classConstraintValue: {
                        classUri: 'http://ontology.tno.nl/logico#Truck',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri:
                                'http://ontology.tno.nl/logico#hasID',
                            operator: 'describedWith',
                            classConstraintValue: {
                                classUri:
                                    'https://w3id.org/sxacml/sample-port/port#LicenseNumber',
                                propertyConditions: {
                                    type: 'condition',
                                    propertyUri:
                                        'http://ontology.tno.nl/logico#hasIDValue',
                                    operator: 'equalTo',
                                    datatypeValue: 'KT3311',
                                },
                            },
                        },
                    },
                },
            ],
        },
    };

    emptyCondition = {
        classUri: 'https://w3id.org/sxacml/sample-port/port#Driver',
        propertyConditions: {},
    };

    render() {
        return (
            <div className='container'>
                <h1>onto-react Demo - class</h1>
                {React.createElement(OntoReact.OntoReact, {
                    mainClass:
                        'https://w3id.org/sxacml/sample-port/port#Driver',
                    //api: false,
                    elementName: 'DriverAuthorizedForTruck',
                    classRelation: 'EQUIVALENT',
                    // headerComponent: OntoReact.InputNameHeader('Individual name'),
                    headerComponent: OntoReact.MultiHeader(
                        OntoReact.InputNameHeader('Class name'),
                        OntoReact.SelectClassHeader('Map class to:')
                    ),
                    api: new OntoReact.Api(false),
                    isDescriptionOfIndividual: false,
                    title:
                        'Add new class mapping for https://w3id.org/sxacml/sample-port/port#Driver',
                    condition: this.fullCondition,
                })}

                <h1>onto-react Demo - individual</h1>
                {React.createElement(OntoReact.OntoReact, {
                    mainClass:
                        'https://w3id.org/sxacml/sample-port/port#Driver',
                    //api: false,
                    elementName: 'ClarkContoso',
                    // headerComponent: OntoReact.InputNameHeader('Individual name'),
                    headerComponent: OntoReact.MultiHeader(
                        OntoReact.InputNameHeader('Individual name'),
                        OntoReact.SelectClassHeader('Instance of class:')
                    ),
                    api: new OntoReact.Api(true),
                    isDescriptionOfIndividual: true,
                    title: 'Define a new individual of type Driver',
                    condition: this.indivCondition,
                })}
            </div>
        );
    }
}

render(<Demo />, document.querySelector('#demo'));
