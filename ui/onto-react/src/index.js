import t from 'prop-types'
import React, {Component} from 'react'

class OntoReact extends Component {
  constructor(props) {
    super(props);

    this.state = {
      results: []
    };
  }

  componentDidMount(){
    fetch("/api/properties/class/PhysicalMemory")
      .then(response => response.json())
      .then(data => {
        this.setState({ results: data})
      });
  }

  render() {
    const results = this.state.results;
    return <div>
        { results.map(res => 
          <p key={res.uri}>{res.localName}: {res.uri}</p>
        )}
      </div>;
  }
}

export default OntoReact