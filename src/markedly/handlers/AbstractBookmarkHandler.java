package markedly.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AbstractBookmarkHandler extends AbstractHandler
{
    protected static IResource getResourceChecked(ExecutionEvent event) throws ExecutionException
    {
        Object resource = HandlerUtil.getActiveEditorInputChecked(event).getAdapter(IResource.class);
        if (resource == null)
        {
            throw new ExecutionException("No resource found while executing " + event.getCommand().getId());
        }
        return (IResource) resource;
    }

    protected static ITextSelection getTextSelectionChecked(ExecutionEvent event) throws ExecutionException
    {
        ISelectionProvider selectionProvider = HandlerUtil.getActiveSiteChecked(event).getSelectionProvider();
        if (selectionProvider == null)
        {
            throw new ExecutionException("No selection provider found while executing " + event.getCommand().getId());
        }
        ISelection selection = selectionProvider.getSelection();
        if (!(selection instanceof ITextSelection))
        {
            throw new ExecutionException("Incorrect type for selection found while executing" + event.getCommand().getId());

        }
        return (ITextSelection) selection;
    }
}
