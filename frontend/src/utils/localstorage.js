const storageObject = {
    setItem: function (key, items) {
        localStorage.setItem(key, JSON.stringify(items))
    },
    getItem: function (key) {
        let val = localStorage.getItem(key) || '[]'
        val = JSON.parse(val)
        return val
    },
    removeItem: function (key) {
        this.setItem(key, [])
    },
}
export default storageObject