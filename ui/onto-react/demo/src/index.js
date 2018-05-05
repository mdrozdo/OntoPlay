import React, { Component } from 'react'
import { render } from 'react-dom'

import OntoReact  from '../../src'


class Demo extends Component {
  state = { loading: false }

  render() {
    return <div className='container'>
      <h1>onto-react Demo</h1>
      {(
        React.createElement(
          OntoReact.OntoReact,
          {
            mainClass: 'http://purl.org/NET/cgo#WorkerNode',
            api: false,
            elementName: 'http://purl.org/NET/cgo#WorkerNode',
            classRelation: 'SUBCLASS',
            //headerComponentName: 'SelectClass'
            headerComponent: OntoReact.SelectClass,
            api: new OntoReact.Api(false),
            title: 'Add new class mapping for http://purl.org/NET/cgo#WorkerNode',
            condition: {
              "classUri": "http://purl.org/NET/cgo#WorkerNode",
              "propertyConditions":
                [{
                  'propertyUri': 'http://gridagents.sourceforge.net/AiGGridOntology#hasGPU',
                  'operator': 'constrainedBy',
                  'classConstraintValue': {
                    'classUri': 'http://gridagents.sourceforge.net/AiGGridOntology#GPU',
                    'propertyConditions': [
                      {
                        'propertyUri': 'http://gridagents.sourceforge.net/AiGGridOntology#hasArchitecture',
                        'operator': 'constrainedBy',
                        'classConstraintValue': {
                          'classUri': 'http://gridagents.sourceforge.net/AiGGridOntology#CPUArchictecture',
                          'propertyConditions': [
                            {
                              'propertyUri': 'http://purl.org/NET/cgo#hasName',
                              'operator': 'equalTo',
                              'datatypeValue': 'PowerPC',
                              'annotations': []
                            }
                          ],
                          'annotations': []
                        },
                        'annotations': []
                      }
                    ],
                    'annotations': []
                  },
                  'annotations': []
                },
                {
                  'propertyUri': 'http://purl.org/NET/cgo#hasName',
                  'operator': 'equalTo',
                  'datatypeValue': 'dupa',
                  'annotations': []
                }
                ]
            }
          })
      )}
    </div>
  }
}

render(<Demo />, document.querySelector('#demo'))
