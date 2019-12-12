package com.r00t.demetapp.models

class LevelModel {
    var columnCount : Int = 0
    var boxesCount : Int = 0

    constructor(boxesCount:Int,columnCount:Int) {
        this.boxesCount=boxesCount
        this.columnCount=columnCount
    }
}