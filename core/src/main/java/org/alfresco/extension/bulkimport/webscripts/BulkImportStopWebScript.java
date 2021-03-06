/*
 * Copyright (C) 2007-2013 Peter Monks.
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
 * 
 * This file is part of an unsupported extension to Alfresco.
 * 
 */

package org.alfresco.extension.bulkimport.webscripts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import org.alfresco.extension.bulkimport.BulkImporter;


/**
 * Web Script class that stops a bulk import, if one is in progress.
 *
 * @author Peter Monks (peter.monks@alfresco.com)
 */
public class BulkImportStopWebScript
    extends DeclarativeWebScript
{
    private final static Log log = LogFactory.getLog(BulkImportStopWebScript.class);
    
    private final BulkImporter importer;
    
    
    public BulkImportStopWebScript(final BulkImporter importer)
    {
        // PRECONDITIONS
        assert importer != null : "importer must not be null.";
        
        //BODY
        this.importer = importer;
    }    

    
    /**
     * @see org.springframework.extensions.webscripts.DeclarativeWebScript#executeImpl(org.springframework.extensions.webscripts.WebScriptRequest, org.springframework.extensions.webscripts.Status, org.springframework.extensions.webscripts.Cache)
     */
    @Override
    protected Map<String, Object> executeImpl(final WebScriptRequest request, final Status status, final Cache cache)
    {
        Map<String, Object> result = new HashMap<String, Object>();

        cache.setNeverCache(true);
        
        if (!importer.getStatus().inProgress())
        {
            result.put("message", "Nothing to stop - no imports are in progress.");
        }
        else if (!importer.getStatus().isStopping())
        {
            importer.stop();
            result.put("message", "Stop requested.");
        }
        else
        {
            result.put("message", "A stop has already been requested.");
        }
        
        return(result);
    }
}
