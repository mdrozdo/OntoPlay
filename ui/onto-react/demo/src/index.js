import React, { Component } from 'react';
import { render } from 'react-dom';
import OntoReact from '../../src';

class Demo extends Component {
    state = { loading: false };

    individualCondition = {
        classUri: 'https://saref.etsi.org/saref4bldg/Building',
        propertyConditions: {
            type: 'values',
            contents: [
                {
                    type: 'condition',
                    propertyUri: 'https://saref.etsi.org/saref4bldg/hasSpace',
                    operator: 'describedWith',
                    classConstraintValue: {
                        classUri:
                            'https://saref.etsi.org/saref4bldg/BuildingSpace',
                        propertyConditions: {
                            type: 'values',
                            contents: [
                                {
                                    type: 'condition',
                                    propertyUri:
                                        'https://w3id.org/sxacml/ontoplay#hasLocalName',
                                    operator: 'equalTo',
                                    datatypeValue: 'floor_1',
                                },
                                {
                                    type: 'condition',
                                    propertyUri:
                                        'https://saref.etsi.org/saref4bldg/hasSpace',
                                    operator: 'describedWith',
                                    classConstraintValue: {
                                        classUri:
                                            'https://saref.etsi.org/saref4bldg/BuildingSpace',
                                        propertyConditions: {
                                            type: 'values',
                                            contents: [
                                                {
                                                    type: 'condition',
                                                    propertyUri:
                                                        'https://w3id.org/sxacml/ontoplay#hasLocalName',
                                                    operator: 'equalTo',
                                                    datatypeValue:
                                                        'server_room_1',
                                                },
                                                {
                                                    type: 'condition',
                                                    propertyUri:
                                                        'https://saref.etsi.org/saref4bldg/contains',
                                                    operator:
                                                        'equalToIndividual',
                                                    valueClassUri:
                                                        'https://w3id.org/sxacml/sample-smartgrid/smartgrid#AirConditioningUnit',
                                                    individualValue:
                                                        'https://w3id.org/sxacml/sample-smartgrid/smartgrid-data#ac_1',
                                                },
                                            ],
                                        },
                                    },
                                },
                                {
                                    type: 'condition',
                                    propertyUri:
                                        'https://saref.etsi.org/saref4bldg/hasSpace',
                                    operator: 'describedWith',
                                    classConstraintValue: {
                                        classUri:
                                            'https://saref.etsi.org/saref4bldg/BuildingSpace',
                                        propertyConditions: {
                                            type: 'values',
                                            contents: [
                                                {
                                                    type: 'condition',
                                                    propertyUri:
                                                        'https://w3id.org/sxacml/ontoplay#hasLocalName',
                                                    operator: 'equalTo',
                                                    datatypeValue:
                                                        'meeting_room_1',
                                                },
                                                {
                                                    type: 'condition',
                                                    propertyUri:
                                                        'https://saref.etsi.org/saref4bldg/contains',
                                                    operator:
                                                        'equalToIndividual',
                                                    valueClassUri:
                                                        'https://w3id.org/sxacml/sample-smartgrid/smartgrid#AirConditioningUnit',
                                                    individualValue:
                                                        'https://w3id.org/sxacml/sample-smartgrid/smartgrid-data#ac_2',
                                                },
                                            ],
                                        },
                                    },
                                },
                            ],
                        },
                    },
                },
                {
                    type: 'condition',
                    propertyUri: 'https://saref.etsi.org/saref4bldg/hasSpace',
                    operator: 'describedWith',
                    classConstraintValue: {
                        classUri:
                            'https://saref.etsi.org/saref4bldg/BuildingSpace',
                        propertyConditions: {
                            type: 'values',
                            contents: [
                                {
                                    type: 'condition',
                                    propertyUri:
                                        'https://w3id.org/sxacml/ontoplay#hasLocalName',
                                    operator: 'equalTo',
                                    datatypeValue: 'floor_2',
                                },
                                {
                                    type: 'condition',
                                    propertyUri:
                                        'https://saref.etsi.org/saref4bldg/hasSpace',
                                    operator: 'describedWith',
                                    classConstraintValue: {
                                        classUri:
                                            'https://saref.etsi.org/saref4bldg/BuildingSpace',
                                        propertyConditions: {
                                            type: 'values',
                                            contents: [
                                                {
                                                    type: 'condition',
                                                    propertyUri:
                                                        'https://w3id.org/sxacml/ontoplay#hasLocalName',
                                                    operator: 'equalTo',
                                                    datatypeValue:
                                                        'maintenance_room',
                                                },
                                                {
                                                    type: 'condition',
                                                    propertyUri:
                                                        'https://saref.etsi.org/saref4bldg/contains',
                                                    operator:
                                                        'equalToIndividual',
                                                    valueClassUri:
                                                        'https://saref.etsi.org/saref4bldg/SpaceHeater',
                                                    individualValue:
                                                        'https://w3id.org/sxacml/sample-smartgrid/smartgrid-data#heater_1',
                                                },
                                                {
                                                    type: 'condition',
                                                    propertyUri:
                                                        'https://saref.etsi.org/saref4bldg/contains',
                                                    operator:
                                                        'equalToIndividual',
                                                    valueClassUri:
                                                        'https://w3id.org/sxacml/sample-smartgrid/smartgrid#WashingMachine',
                                                    individualValue:
                                                        'https://w3id.org/sxacml/sample-smartgrid/smartgrid-data#washingMachine_1',
                                                },
                                            ],
                                        },
                                    },
                                },
                            ],
                        },
                    },
                },
                {
                    type: 'condition',
                    propertyUri: 'https://saref.etsi.org/saref4bldg/hasSpace',
                    operator: 'describedWith',
                    classConstraintValue: {
                        classUri:
                            'https://saref.etsi.org/saref4bldg/BuildingSpace',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri:
                                'https://w3id.org/sxacml/ontoplay#hasLocalName',
                            operator: 'equalTo',
                            datatypeValue: 'floor_3',
                        },
                    },
                },
            ],
        },
    };

    classCondition = {
        classUri: 'https://w3id.org/sxacml/access-control#PermittedRequest',
        propertyConditions: {
            type: 'intersection',
            contents: [
                {
                    type: 'condition',
                    propertyUri:
                        'https://w3id.org/sxacml/access-control#concernsAction',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri:
                            'https://w3id.org/sxacml/sample-smartgrid/smartgrid#SetTargetTemperatureCommand',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri:
                                'https://w3id.org/sxacml/sample-smartgrid/smartgrid#targetTemperatureCelsius',
                            operator: 'greaterThan',
                            datatypeValue: '25',
                        },
                    },
                },
                {
                    type: 'condition',
                    propertyUri:
                        'https://w3id.org/sxacml/access-control#concernsResource',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri:
                            'https://w3id.org/sxacml/sample-smartgrid/smartgrid#AirConditioningUnit',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri:
                                'https://saref.etsi.org/saref4bldg/isContainedIn',
                            operator: 'constrainedBy',
                            classConstraintValue: {
                                classUri:
                                    'https://w3id.org/sxacml/sample-smartgrid/smartgrid#MeetingRoom',
                                propertyConditions: [{}],
                            },
                        },
                    },
                },
                {
                    type: 'condition',
                    propertyUri:
                        'https://w3id.org/sxacml/access-control#inContextOf',
                    operator: 'constrainedBy',
                    classConstraintValue: {
                        classUri:
                            'https://w3id.org/sxacml/access-control#Environment',
                        propertyConditions: {
                            type: 'condition',
                            propertyUri:
                                'urn:oasis:names:tc:xacml:1.0:environment:current-time',
                            operator: 'greaterThan',
                            datatypeValue: '19:00',
                        },
                    },
                },
            ],
        },
    };

    classMappingCondition = {
        classUri: 'https://saref.etsi.org/core/HVAC',
        classRelation: 'EQUIVALENT',
        propertyConditions: {},
    };

    render() {
        return (
            <span>
                <div className='container'>
                    <h1>onto-react Demo - Individual</h1>
                    {React.createElement(OntoReact.OntoReact, {
                        mainClass: 'https://saref.etsi.org/saref4bldg/Building',
                        //api: false,
                        elementName: 'a_building',
                        isDescriptionOfIndividual: true,
                        // headerComponent: OntoReact.InputNameHeader('Individual name'),
                        headerComponent: OntoReact.MultiHeader(
                            OntoReact.InputNameHeader('Individual name'),
                            OntoReact.SelectClassHeader('Of class:')
                        ),
                        api: new OntoReact.Api(true),
                        title:
                            'Add new individual of class https://saref.etsi.org/saref4bldg/Building',
                        condition: this.individualCondition,
                    })}
                </div>

                <div className='container'>
                    <h1>onto-react Demo - Class</h1>
                    {React.createElement(OntoReact.OntoReact, {
                        mainClass:
                            'https://w3id.org/sxacml/access-control#PermittedRequest',
                        //api: false,
                        elementName: 'Temperature_change_permission',
                        isDescriptionOfIndividual: false,
                        // headerComponent: OntoReact.InputNameHeader('Individual name'),
                        headerComponent: OntoReact.InputNameHeader(
                            'Class name'
                        ),
                        api: new OntoReact.Api(false),
                        title: 'Add new permission',
                        condition: this.classCondition,
                    })}
                </div>

                <div className='container'>
                    <h1>onto-react Demo - Class mapping</h1>
                    {React.createElement(OntoReact.OntoReact, {
                        mainClass: 'https://saref.etsi.org/core/HVAC',
                        //api: false,
                        elementName:
                            'https://w3id.org/sxacml/sample-smartgrid/testRequest#ClimateControlDevice',
                        isDescriptionOfIndividual: false,
                        // headerComponent: OntoReact.InputNameHeader('Individual name'),
                        headerComponent: OntoReact.MultiHeader(
                            OntoReact.InputNameHeader('Class name: '),
                            OntoReact.SelectClassHeader('Equivalent to class:')
                        ),
                        classRelation: 'EQUIVALENT',
                        api: new OntoReact.Api(false),
                        title: 'Add new class mapping',
                        condition: this.classMappingCondition,
                    })}
                </div>
            </span>
        );
    }
}

render(<Demo />, document.querySelector('#demo'));
