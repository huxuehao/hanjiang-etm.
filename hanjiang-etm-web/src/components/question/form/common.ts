import { h } from "vue";
export const chineseNumbers = [
  "零",
  "一",
  "二",
  "三",
  "四",
  "五",
  "六",
  "七",
  "八",
  "九",
];

export const prop = {
  value: "id",
  label: "name",
  children: "children",
};

// 为table头添加*
export const addReadStar = (data: { column: any; $index: number }) => {
  return [
    h(
      "span",
      {
        style: "color: #f67a7a",
      },
      "*"
    ),
    h("span", " " + data.column.label),
  ];
};
