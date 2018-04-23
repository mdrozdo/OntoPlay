import React, {Component} from 'react'
import {render} from 'react-dom'

import OntoReact from '../../src'

class Demo extends Component {
  state = { loading: false }

  render() {
    return <div>
      <h1>onto-react Demo</h1>
      <OntoReact/>
    </div>
  }
}

render(<Demo/>, document.querySelector('#demo'))
