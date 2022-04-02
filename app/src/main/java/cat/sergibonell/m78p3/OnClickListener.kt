package cat.sergibonell.m78p3

import cat.sergibonell.m78p3.data.PostData

interface OnClickListener {
    fun onClickDelete(post: PostData)
    fun onClickEdit(post: PostData)
}