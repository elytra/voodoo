package voodoo.gui.view

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXListView
import com.jfoenix.controls.JFXTextField
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*
import voodoo.curse.CurseClient
import voodoo.gui.controller.EntryController
import voodoo.gui.controller.ModpackController
import voodoo.gui.extensions.jfxbutton
import voodoo.gui.extensions.jfxcombobox
import voodoo.gui.extensions.jfxlistview
import voodoo.gui.extensions.jfxtextfield
import voodoo.gui.model.EntryModel
import voodoo.gui.model.ModpackModel
import voodoo.provider.Provider

/**
 * Created by nikky on 18/03/18.
 * @author Nikky
 * @version 1.0
 */
class ModpackEditorView : View("Modpack Editor") {
    val controller: ModpackController by inject()
    val model: ModpackModel by inject()

    override val root = form {
        val modpack = controller.selectedModpack
        fieldset("Edit Modpack") {
            vgrow = Priority.ALWAYS //important

            field("Name") {
                jfxtextfield(modpack.name) {
                    required()
                }
            }
            field("Title") {
                jfxtextfield(modpack.title) {
                    required()
                }
            }
            field("Version") {
                jfxtextfield(modpack.version) {
                    required()
                }
            }

            jfxbutton("ADD Author") {
                action {
                    modpack.authors.add(SimpleStringProperty("new")) }
            }

            //TODO fix size, make sure it marks modpack as dirty

            jfxlistview<HBox> {
                items.bind(modpack.authors) { authorProp ->
                    hbox {
                        jfxtextfield(authorProp) {
//                            onEdit { editEventType, hBox -> save() }
                        }
                        jfxbutton("-") {
                            action {
                                modpack.authors.remove(authorProp)
                            }
                        }
                    }

                }
            }

            jfxbutton("Save", type = JFXButton.ButtonType.RAISED) {
                enableWhen(modpack.dirty)
                action {
                    modpack.commit()
                }
                style {
                    backgroundColor += Color.WHITE
                }
                prefWidth = 80.0
                spacing = 5.0
            }
            jfxbutton("Reset", type = JFXButton.ButtonType.RAISED) {
                action {
                    modpack.rollback()
                }
                style {
                    backgroundColor += Color.WHITE
                }
                prefWidth = 80.0
                spacing = 5.0
            }

//            text("\nResult:\n")
//            jfxtextarea(entry.resultJson) {
//                vgrow = Priority.SOMETIMES //important
//                isEditable = false
//            }
        }
    }

    private fun save() {
        model.validationContext.validate(true)
        model.commit()
    }
}
