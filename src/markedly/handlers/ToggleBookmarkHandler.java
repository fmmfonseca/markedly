package markedly.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class ToggleBookmarkHandler extends AbstractBookmarkHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        try
        {
            IResource resource = getResourceChecked(event);
            ITextSelection textSelection = getTextSelectionChecked(event);
            int selectionEnd = textSelection.getOffset() + textSelection.getLength();
            int selectionStart = textSelection.getOffset();
            for (IMarker bookmark : resource.findMarkers(IMarker.BOOKMARK, true, IResource.DEPTH_INFINITE))
            {
                if (selectionStart == MarkerUtilities.getCharStart(bookmark) && selectionEnd == MarkerUtilities.getCharEnd(bookmark))
                {
                    bookmark.delete();
                    return null;
                }
            }
            IMarker marker = resource.createMarker(IMarker.BOOKMARK);
            marker.setAttribute(IMarker.MESSAGE, "Bookmark");
            marker.setAttribute(IMarker.CHAR_END, selectionEnd);
            marker.setAttribute(IMarker.CHAR_START, selectionStart);
            marker.setAttribute(IMarker.LINE_NUMBER, textSelection.getStartLine());
        }
        catch (ExecutionException e)
        {
            // Ignore
        }
        catch (CoreException e)
        {
            throw new ExecutionException(e.getMessage(), e.getCause());
        }
        return null;
    }
}
