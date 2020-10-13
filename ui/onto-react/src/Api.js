class Api {
    constructor(isDescriptionOfIndividual) {
        this.isDescriptionOfIndividual = isDescriptionOfIndividual;
    }

    propertiesByClassName = {};
    propertyPromisesByClassName = {};

    cache = {};    
    cachingFetchJSON(url) {
        if (!this.cache.hasOwnProperty(url)) {
            this.cache[url] = fetch(url).then(resp => resp.json());
        } 
        return this.cache[url];
    }    

    getIndividuals(className) {
        return fetch(
            '/api/individuals/class/' + encodeURIComponent(className)
        ).then((response) => response.json());
    }

    getProperties(className) {
        return this.cachingFetchJSON('/api/properties/class/' + encodeURIComponent(className))
            .then((result) => {
                this.propertiesByClassName[className] = result;
                return result;
            });        
    }

    getOperators(propertyUri) {
        return fetch(
            '/api/properties/operators/' +
                encodeURIComponent(propertyUri) +
                '/' +
                this.isDescriptionOfIndividual
        ).then((response) => response.json());
    }

    getIndividualDataForUpdate(individualName) {
        return fetch(
            '/api/individuals/data/' + individualName
        ).then((response) => response.json());
    }

    getClasses(className, propertyName) {
        return this.cachingFetchJSON('/api/class/' + encodeURIComponent(className) + '/property/' + encodeURIComponent(propertyName));
    }

    getAllClasses() {
        return this.cachingFetchJSON('/api/class');
    }

    add(data, elementName) {
        var url = '';
        if (this.isDescriptionOfIndividual) url = '/api/individuals/save';
        else url = '/api/class/save';
        var dataToSend = { conditionJson: data, name: elementName };

        return fetch(url, {
            method: 'POST',
            body: JSON.stringify(dataToSend),
            headers: new Headers({ 'Content-Type': 'application/json' }),
        }).then((response) => response.json());
    }

    getAnnotationProperties(componentUri) {
        return fetch(
            '/api/annotationProperties/get/' + encodeURIComponent(componentUri)
        ).then((response) => response.json());
    }
}

export default Api;