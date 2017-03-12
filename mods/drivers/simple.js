class SimpleDriver {
  parse (type, mod, data) {
    let result = {}
    const meta = data.split(',')
    for (let e in meta) {
      meta[e] = meta[e].split(':')
      for (let i in mod[type]) {
        if (mod[type][i].label === meta[e][0]) result[mod[type][i].label] = this.format(meta[e][1], mod[type][i].format)
      }
    }
    return result
  }

  encode (obj) {
    let t = ''
    for (let i in obj) {
      t += i + ':' + obj[i] + ','
    }
    return t.substring(0, t.length - 1)
  }

  // 格式化数据中字符串为数据点定义中的格式
  format (data, format) {
    let result
    switch (format.type) {
      case 'int':
        result = parseInt(data)
        break
      case 'float':
        result = parseFloat(parseFloat(data).toFixed(format.round))
        break
      case 'boolean':
        if (parseInt(data) === 1 || data === 'true'){
          result = true
        } else {
          result = false
        }
        break
      default:
        result = data
        break
    }
    return result
  }
}

module.exports = new SimpleDriver()
