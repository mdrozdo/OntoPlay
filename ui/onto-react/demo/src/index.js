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
                    title:
                        'Define a new individual of type Driver',
                    condition: this.emptyCondition,
                })}
            </div>
        );
    }
}

render(<Demo />, document.querySelector('#demo'));
