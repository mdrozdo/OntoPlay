module.exports = {
  type: 'react-component',
  npm: {
    esModules: true,
    umd: {
      global: 'OntoReact',
      externals: {
        react: 'React'
      }
    }
  },
  // webpack: {
  //   extra: {
  //     output: {
  //       library: 'ontoReact' 
  //     }
  //   }
  // },
  "devServer": {
    "proxy": {
      "/api": {
        "target": "http://localhost:9000",
        "secure": false
      }
    }
  }
}
