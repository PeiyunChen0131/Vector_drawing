package a2.views

import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem

class SketchItMenuView: MenuBar() {
    var fileMenu = Menu("File")
    var editMenu = Menu("Edit")
    var helpMenu = Menu("Help")
    var itemNew = MenuItem("New")
    var itemLoad = MenuItem("Load")
    var itemSave = MenuItem("Save")
    var itemQuit = MenuItem("Quit")
    var itemCut = MenuItem("Cut")
    var itemCopy = MenuItem("Copy")
    var itemPaste = MenuItem("Paste")
    var itemAbout = MenuItem("About")

    init {
        fileMenu.items.add(itemNew)
        fileMenu.items.add(itemLoad)
        fileMenu.items.add(itemSave)
        fileMenu.items.add(itemQuit)

        editMenu.items.add(itemCut)
        editMenu.items.add(itemCopy)
        editMenu.items.add(itemPaste)

        helpMenu.items.add(itemAbout)

        menus.add(fileMenu)
        menus.add(editMenu)
        menus.add(helpMenu)
    }
}