package markedly.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.MarkerUtilities;

import java.util.Arrays;
import java.util.Comparator;

public class NextBookmarkHandler extends AbstractBookmarkHandler
{
    private static final Comparator<IMarker> OFFSET_ASCENDING_COMPARATOR = new Comparator<IMarker>()
    {
        @Override
        public int compare(IMarker m1, IMarker m2)
        {
            return MarkerUtilities.getCharStart(m1) - MarkerUtilities.getCharStart(m2);
        }
    };

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        try
        {
            IResource resource = getResourceChecked(event);
            ITextSelection textSelection = getTextSelectionChecked(event);
            int selectionStart = textSelection.getOffset();
            IMarker[] bookmarks = resource.findMarkers(IMarker.BOOKMARK, true, IResource.DEPTH_INFINITE);
            if (bookmarks.length < 1)
            {
                return null;
            }
            Arrays.sort(bookmarks, OFFSET_ASCENDING_COMPARATOR);
            IMarker nextBookmark = bookmarks[0];
            for (IMarker bookmark : bookmarks)
            {
                if (MarkerUtilities.getCharStart(bookmark) > selectionStart)
                {
                    nextBookmark = bookmark;
                    break;
                }
            }
            IDE.openEditor(HandlerUtil.getActiveSiteChecked(event).getPage(), nextBookmark, OpenStrategy.activateOnOpen());
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
