/**
 * Copyright © 2017 Robin Weiss (http://www.gerdi-project.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gerdiproject.astyle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.gerdiproject.astyle.listeners.AutoFormatChangedListener;

/**
 * The activator class controls the plug-in life cycle.
 * It is initialized early in order to fix a bug that causes the Auto-Format
 * option to not work if no formatting was triggered manually before within the same session.
 */
public class Activator extends AbstractUIPlugin implements IStartup
{
    // The plug-in ID
    public static final String PLUGIN_ID = "AStyle_Neon"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;


    /**
     * The constructor
     */
    public Activator()
    {
    }


    @Override
    public void earlyStartup()
    {
        // nothing to do here
        // at this point, the Activator was already initialized
    }


    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        plugin.getPreferenceStore().addPropertyChangeListener(new AutoFormatChangedListener());
    }


    @Override
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }


    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return plugin;
    }


    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
