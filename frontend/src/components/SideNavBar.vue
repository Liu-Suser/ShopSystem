<template>
  <el-aside width="200px">
    <el-menu
      default-active="/"
      class="side-nav"
      mode="vertical"
      :default-openeds="category.map(item => item.id+'')"
    >
      <el-submenu
        v-for="a in category"
        :key="a.id"
        :index="a.id+''"
      >
        <template slot="title">{{ a.name }}</template>
        <el-menu-item
          v-for="b in a.childern"
          :key="b.id"
          :index="b.id+''"
          @click="pushCategory(b)"
        >
          {{ b.name }}</el-menu-item>
      </el-submenu>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  data() {
    return {
      category: [],
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath)
    },
    filterCategory(res) {
      res.forEach((a) => {
        if (a.parentId == 0) {
          let cate = new Array()
          cate.id = a.id
          cate.name = a.name
          cate.status = a.status
          cate.childern = new Array()
          this.category.push(cate)
        } else {
          let child = new Array()
          child.id = a.id
          child.name = a.name
          child.status = a.status
          this.category.forEach((b) => {
            if (b.id == a.parentId) {
              b.childern.push(child)
            }
          })
        }
      })
    },
    getAllCategory() {
      this.$axios
        .get('/category')
        .then((res) => {
          if (res.status == 200) {
            this.filterCategory(res.data)
          }
        })
        .catch(() => {})
    },
    pushCategory(category) {
      this.$router.push({ path: '/category', query: { id: category.id } })
    },
  },
  created: function () {
    this.getAllCategory()
  },
}
</script>

<style>
  .side-nav {
    width: 200px;
  }
</style>
