/*
 *  Copyright © 2018 Robin Weiss (http://www.gerdi-project.de/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package astyle_neon.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This helper class offers static methods for retrieving Eclipse UI elements.
 * @author Robin Weiss
 */
public class AStyleEclipseUtils
{
    /**
     * Private constructor, because this is just a collection of useful methods.
     */
    private AStyleEclipseUtils()
    {

    }


    /**
     * Retrieves Eclipse action bars.
     *
     * @param event the event that triggered the message
     * @return Eclipse action bars
     */
    public static IActionBars getActionBars(ExecutionEvent event)
    {
        IEditorPart editor;

        try {
            editor = HandlerUtil.getActiveWorkbenchWindowChecked(event)
                     .getActivePage()
                     .getActiveEditor();
        } catch (ExecutionException e) {
            editor = null;
        }

        // check if there is an active editor
        if (editor != null)
            return editor.getEditorSite().getActionBars();
        else {
            IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
            return ((IViewSite) activePart.getSite()).getActionBars();
        }
    }


    /**
     * Attempts to retrieve the project that belongs to the active editor.
     *
     * @return the project of the active editor, or null if there is no active editor
     */
    public static IProject getActiveProject()
    {
        IProject currentProject = null;

        // get active editor
        final IEditorPart activeEditor = PlatformUI.getWorkbench()
                                         .getActiveWorkbenchWindow()
                                         .getActivePage()
                                         .getActiveEditor();

        // get an adaptable in order to retrieve its project
        IAdaptable adaptable = null;

        if (activeEditor != null)
            adaptable = activeEditor.getEditorInput();

        if (adaptable != null) {
            // retrieve current project from adaptable
            currentProject = adaptable.getAdapter(IProject.class);

            // fallback: get a resource from the adaptable and then its project
            if (currentProject == null) {
                final IResource resource = adaptable.getAdapter(IResource.class);

                if (resource != null)
                    currentProject = resource.getProject();
            }
        }

        return currentProject;
    }


    /**
     * Retrieves the currently active Project in Eclipse via an event.
     *
     * @param event
     *            the event that triggered the command
     * @return a project that is associated with a currently selected file or
     *         editor view
     * @throws ExecutionException this exception can occur while trying to retrieve the active window
     */
    public static IProject getActiveProject(ExecutionEvent event) throws ExecutionException
    {
        IProject currentProject = null;

        // check which window is focussed
        boolean isExplorerFocussed = HandlerUtil.getActivePartId(event).endsWith(AStyleHandlerConstants.EXPLORER_SUFFIX);
        final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        // get an adaptable in order to retrieve its project
        IAdaptable adaptable = null;

        if (isExplorerFocussed) {
            // get selection
            final IStructuredSelection selection =
                (IStructuredSelection) window.getSelectionService().getSelection();

            if (!selection.isEmpty())
                adaptable = (IAdaptable) selection.getFirstElement();
        }

        // fallback: choose the project of the active editor
        if (adaptable == null) {
            // get active editor
            final IEditorPart activeEditor = window.getActivePage().getActiveEditor();

            if (activeEditor != null)
                adaptable = activeEditor.getEditorInput();
        }

        if (adaptable != null) {
            // retrieve current project from adaptable
            currentProject = adaptable.getAdapter(IProject.class);

            // fallback: get a resource from the adaptable and then its project
            if (currentProject == null) {
                final IResource resource = adaptable.getAdapter(IResource.class);

                if (resource != null)
                    currentProject = resource.getProject();
            }
        }

        return currentProject;
    }
}
