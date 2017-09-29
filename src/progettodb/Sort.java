package progettodb;

public class Sort
{
    public static void mergeSort(Comparable [ ] a)
    {
            Comparable[] tmp = new Comparable[a.length];
            mergeSort(a, tmp,  0,  a.length - 1);
    }


    private static void mergeSort(Comparable [ ] a, Comparable [ ] tmp, int left, int right)
    {
            if( left < right )
            {
                    int center = (left + right) / 2;
                    mergeSort(a, tmp, left, center);
                    mergeSort(a, tmp, center + 1, right);
                    merge(a, tmp, left, center + 1, right);
            }
    }


    private static void merge(Comparable[ ] a, Comparable[ ] tmp, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(a[left].compareTo(a[right]) <= 0)
                tmp[k++] = a[left++];
            else
                tmp[k++] = a[right++];

        while(left <= leftEnd)    // Copy rest of first half
            tmp[k++] = a[left++];

        while(right <= rightEnd)  // Copy rest of right half
            tmp[k++] = a[right++];

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
            a[rightEnd] = tmp[rightEnd];
    }
    
    public static void pMergeSort(Comparable [ ] a, Comparable[] b )
    {
            Comparable[] tmp = new Comparable[a.length];
            Comparable[] tmp2 = new Comparable[b.length];
            pMergeSort(a, tmp, b, tmp2,  0,  a.length - 1);
    }


    private static void pMergeSort(Comparable [ ] a, Comparable [ ] tmp, Comparable[] b, Comparable[] tmp2, int left, int right)
    {
            if( left < right )
            {
                    int center = (left + right) / 2;
                    pMergeSort(a, tmp, b, tmp2, left, center);
                    pMergeSort(a, tmp, b, tmp2, center + 1, right);
                    pMerge(a, tmp, b, tmp2, left, center + 1, right);
            }
    }


    private static void pMerge(Comparable[ ] a, Comparable[ ] tmp, Comparable[] b, Comparable[] tmp2, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(a[left].compareTo(a[right]) <= 0)
            {
                tmp2[k] = b[left];
                tmp[k++] = a[left++];
            }
            else
            {
                tmp2[k] = b[right];
                tmp[k++] = a[right++];
            }

        while(left <= leftEnd)    // Copy rest of first half
        {
            tmp2[k] = b[left];
            tmp[k++] = a[left++];
        }

        while(right <= rightEnd)  // Copy rest of right half
        {
            tmp2[k] = b[right];
            tmp[k++] = a[right++];
        }

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
        {
            a[rightEnd] = tmp[rightEnd];
            b[rightEnd] = tmp2[rightEnd];
        }
    }
 }