class Api {
    constructor(isDescriptionOfIndividual) {
        this.isDescriptionOfIndividual = isDescriptionOfIndividual;
    }

    getIndividuals(className) {
        return fetch('/api/individuals/class/' + encodeURIComponent(className))
            .then(response => response.json());
    }

    getProperties(className) {
        return fetch('/api/properties/class/' + encodeURIComponent(className))
            .then(response => response.json());
    }

    getOperators(propertyUri) {
        return fetch('/api/properties/operators/' + encodeURIComponent(propertyUri) + '/' + this.isDescriptionOfIndividual)
            .then(response => response.json());
    }

    getIndividualDataForUpdate(individualName) {
        return fetch('/api/individuals/data/' + individualName)
            .then(response => response.json());
    }

    getClasses(propertyName) {
        return fetch('/api/class/property/' + encodeURIComponent(propertyName))
            .then(response => response.json());
    }

    getAllClasses() {
        return fetch('/api/class')
            .then(response => response.json());
    }

    add(data, elementName) {
        var url = '';
        if (this.isDescriptionOfIndividual)
            url = '/api/individuals/save';
        else
            url = '/api/class/save';
        var dataToSend = { 'conditionJson': data, 'name': elementName };

        return fetch(url, {
            method: 'POST',
            body: JSON.stringify(dataToSend),
            headers: new Headers({ 'Content-Type': 'application/json' })
        })
            .then(response => response.json());
    }

    getAnnotationProperties(componentUri) {
        return fetch('/api/annotationProperties/get/' + encodeURIComponent(componentUri))
            .then(response => response.json());
    }
}

export default Api;